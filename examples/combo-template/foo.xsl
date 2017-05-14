<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:output omit-xml-declaration="yes"/>

    <xsl:template match="/*">
        <foo>
            <xsl:next-match/>
        </foo>
    </xsl:template>

</xsl:stylesheet>
