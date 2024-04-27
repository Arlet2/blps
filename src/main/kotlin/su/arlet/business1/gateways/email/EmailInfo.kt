package su.arlet.business1.gateways.email

import su.arlet.business1.gateways.email.letters.Letter

class EmailInfo (
    var to : String,
    var letter: Letter,
) {
    constructor() : this("", Letter())
}