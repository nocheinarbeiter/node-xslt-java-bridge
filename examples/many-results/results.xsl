<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:output omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <xsl:result-document href="res1.xml"><result-1/></xsl:result-document>
        <xsl:result-document href="{resolve-uri('res2.xml', base-uri())}"><result-2/></xsl:result-document>
        <xsl:result-document href="{resolve-uri('res3.xml')}"><result-3/></xsl:result-document>
        <xsl:result-document href="{resolve-uri('res4 with spaces.xml')}"><result-4/></xsl:result-document>
        <main-result/>
    </xsl:template>

</xsl:stylesheet>
