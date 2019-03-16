<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Edited with XML Spy v2007 (http://www.altova.com) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output version="1.0" indent="yes" encoding="UTF-8" method="html"/>
    <xsl:template match="/">
        <html>
            <body>
                <h2>My CD Collection</h2>
                <table border="1">
                    <xsl:for-each select="document/comments">
                        <tr>
                            <td>
                                <xsl:value-of select="id"/>
                            </td>
                            <td>
                                <xsl:value-of select="content"/>
                            </td>
                            <td>
                                <xsl:value-of select="who/nickname"/>
                            </td>
                            <td>
                                <xsl:value-of select="who/id"/>
                            </td>
                        </tr>
                    </xsl:for-each>

                    <xsl:for-each select="document">
                        <tr>
                            <td>
                                <xsl:value-of select="emotions"/>
                            </td>
                            <td>
                                <xsl:value-of select="postedAt"/>
                            </td>
                            <td>
                                <xsl:value-of select="author/nickname"/>
                            </td>
                            <td>
                                <xsl:value-of select="author/id"/>
                            </td>
                        </tr>
                    </xsl:for-each>

                    <xsl:for-each select="document">
                        <tr>
                            <td>
                                <xsl:value-of select="id"/>
                            </td>
                            <td>
                                <xsl:value-of select="content"/>
                            </td>
                            <td>
                            </td>
                            <td>
                            </td>
                        </tr>
                    </xsl:for-each>

                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>