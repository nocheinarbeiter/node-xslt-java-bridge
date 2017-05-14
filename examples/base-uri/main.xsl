<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

    <xsl:output omit-xml-declaration="yes" indent="yes"/>

    <xsl:template match="/main">
        <xsl:next-match/>
        <xsl:apply-templates select="document('sub.xml')"/>
    </xsl:template>

    <xsl:template match="*">
        <base-uri value="{base-uri()}" element="{name()}"/>
    </xsl:template>

</xsl:stylesheet>
