package su.arlet.business1.gateways.email.letters

abstract class Letter (
    val name : String,
    val subject : String,
) {
    abstract fun getHtml() : String
}