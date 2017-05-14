<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:output omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <xsl:result-document href="res1.xml"><result-1/></xsl:result-document>
        <xsl:result-document href="res2.xml"><result-2/></xsl:result-document>
        <main-result/>
    </xsl:template>

</xsl:stylesheet>
