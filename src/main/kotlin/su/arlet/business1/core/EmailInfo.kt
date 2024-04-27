package su.arlet.business1.core

import su.arlet.business1.core.letters.Letter

class EmailInfo (
    var to : String,
    var letter: Letter,
) {
    constructor() : this("", Letter())
}