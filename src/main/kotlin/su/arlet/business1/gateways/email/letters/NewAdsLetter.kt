package su.arlet.business1.gateways.email.letters

import su.arlet.business1.core.AdPost

class NewAdsLetter(
    private val newAds: List<AdPost>,
) : Letter("new-ads-letter", "New ads for last day!") {
    override fun getHtml(): String {
        var text = ""

        newAds.forEach {ad ->
            text += "<h1>${ad.title}</h1></br>"
            text += "<p>${ad.body}</p></br>"
            text += "<p>Image attached: ${ad.image?.link ?: "no"}</p></br>"
            text += "<p>Should be published until: ${ad.adRequest.publishDeadline ?: "no deadline"}</p></br>"
            text += "<p>Life hours: ${ad.adRequest.lifeHours}</p></br>"
            text += "</br></br></br>"
        }

        return """
            <html>
                <body>
                    $text
                </body>
            </html>
        """.trimIndent()
    }
}