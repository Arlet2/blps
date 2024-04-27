package su.arlet.business1.core.letters

import java.io.Serializable

open class Letter (
    val name : String,
    val subject : String,
) : Serializable {

    open var html : String = ""
    constructor() : this("", "")
}