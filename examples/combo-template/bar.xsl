<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:output omit-xml-declaration="yes"/>

    <xsl:template match="/*">
        <bar>
            <xsl:next-match/>
        </bar>
    </xsl:template>

</xsl:stylesheet>
