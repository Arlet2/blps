package su.arlet.business1.gateways.email.letters

class TestLetter(
    private val testStr: String,
) : Letter("test-letter", "Test letter") {
    override fun getHtml(): String {
        return """
            <html>
                <body>
                    <p>${testStr}</p>
                </body>
            </html>
        """.trimIndent()
    }
}