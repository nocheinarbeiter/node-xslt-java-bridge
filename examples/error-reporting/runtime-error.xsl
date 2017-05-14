<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:output omit-xml-declaration="yes"/>

    <xsl:template match="divide">
        <xsl:value-of select="@number div @by"/>
    </xsl:template>

    <xsl:template match="divide[@by = 0]">
        <xsl:message terminate="yes" select="'Expecting non-zero divisor'"/>
    </xsl:template>

</xsl:stylesheet>
