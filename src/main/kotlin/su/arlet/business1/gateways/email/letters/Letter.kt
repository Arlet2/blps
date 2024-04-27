package su.arlet.business1.gateways.email.letters

import java.io.Serializable

open class Letter (
    val name : String,
    val subject : String,
) : Serializable {

    open var html : String = ""
    constructor() : this("", "")
}