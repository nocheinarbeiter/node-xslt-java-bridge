import net.sf.saxon.lib.StandardLogger;
import net.sf.saxon.s9api.*;
import net.sf.saxon.Configuration;
import net.sf.saxon.lib.OutputURIResolver;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class Transformer
{
    private XsltTransformer transformer;
    private String primaryOutputMethod;
    private DocumentBuilder documentBuilder;
    private CustomSerializer serializer;
    private ArrayList<VirtualFile> results;
    private ByteArrayOutputStream errorLogStream;

    public Transformer(String xsltPath) throws Exception
    {
        this(new StreamSource(new File(xsltPath)));
    }

    public Transformer(String[] xsltPaths) throws Exception
    {
        this(buildXsltIndexSource(xsltPaths));
    }

    private static StreamSource buildXsltIndexSource(String[] xsltPaths) {
        if (xsltPaths.length == 1) {
            return new StreamSource(new File(xsltPaths[0]));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"3.1\">");
        for (String xsltPath : xsltPaths) {
            sb.append("<xsl:include href=\"");
            sb.append(xsltPath);
            sb.append("\"/>");
        }
        sb.append("</xsl:stylesheet>");
        return new StreamSource(new StringReader(sb.toString()));
    }

    private Transformer(StreamSource xsltSource) throws Exception
    {
        Configuration config = new Configuration();

        StandardLogger logger = (StandardLogger) config.getLogger();
        logger.setThreshold(StandardLogger.ERROR);
        this.errorLogStream = new ByteArrayOutputStream();
        config.setStandardErrorOutput(new PrintStream(this.errorLogStream));

        config.setOutputURIResolver(new OutputResolver());

        Processor processor = new Processor(config);
        this.documentBuilder = processor.newDocumentBuilder();
        XsltCompiler compiler = processor.newXsltCompiler();

        XsltExecutable xsltExecutable;
        try
        {
            xsltExecutable = compiler.compile(xsltSource);
        }
        catch (SaxonApiException e)
        {
            throw new Exception(this.extractErrorMessage(), e);
        }
        this.transformer = xsltExecutable.load();

        this.serializer = new CustomSerializer(processor);

        // side-effect: modifies the supplied Serializer to make it aware
        // of the serialization properties defined in the default xsl:output declaration of the stylesheet
        // however, they have no effect on any output produced using xsl:result-document
        this.transformer.setDestination(serializer);
        this.primaryOutputMethod = this.serializer.getOutputMethod();

        this.results = new ArrayList<>();
    }

    public ArrayList<VirtualFile> transform(String sourceBase, String sourcePath, String sourceContents) throws Exception
    {
        this.results.clear();

        StringWriter writer = new StringWriter();
        this.serializer.setOutputWriter(writer);

        // BaseOutputURI used for resolving relative URIs in the href attribute of the xsl:result-document
        this.transformer.setBaseOutputURI(sourceBase);

        this.documentBuilder.setBaseURI(new URI("file:" + sourcePath));

        try
        {
            XdmNode docSource = this.documentBuilder.build(new StreamSource(new StringReader(sourceContents)));
            this.transformer.setInitialContextNode(docSource);
            this.transformer.transform();
        }
        catch (SaxonApiException e)
        {
            throw new Exception(this.extractErrorMessage(), e);
        }

        String resultContents = writer.toString();

        this.results.add(0, new VirtualFile(sourceBase, sourcePath, resultContents, this.primaryOutputMethod));
        return this.results;
    }

    private String extractErrorMessage() {
        String message = new String(this.errorLogStream.toByteArray(), StandardCharsets.UTF_8);
        this.errorLogStream.reset();
        return message;
    }

    private class VirtualFile
    {
        public String base;
        public String path;
        public String contents;
        public String serializationMethod;

        VirtualFile(String base, String path, String contents, String method)
        {
            this.base = base;
            this.path = path;
            this.contents = contents;
            this.serializationMethod = method;
        }
    }

    private class CustomSerializer extends Serializer
    {
        CustomSerializer(Processor proc) { super(proc); }

        String getOutputMethod()
        {
            return this.getCombinedOutputProperties().getProperty(Property.METHOD.toString());
        }
    }

    private class OutputResolver implements OutputURIResolver
    {
        String base;
        String path;

        public OutputURIResolver newInstance()
        {
            return new OutputResolver();
        }

        public Result resolve(String href, String base) throws TransformerException
        {
            this.base = base;
            this.path = href;
            return new StreamResult(new StringWriter());
        }

        public void close(Result result) throws TransformerException
        {
            String resultContents = ((StreamResult) result).getWriter().toString();
            // can not get serialization method for output produced using xsl:result-document
            results.add(new VirtualFile(this.base, this.path, resultContents, null));
        }
    }

}
