package su.arlet.core

import su.arlet.core.letters.Letter

class EmailInfo (
    var to : String,
    var letter: Letter,
) {
    constructor() : this("", Letter())
}