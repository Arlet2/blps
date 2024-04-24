package su.arlet.business1.gateways.email

import org.springframework.stereotype.Service
import su.arlet.business1.gateways.email.letters.Letter

@Service
class EmailGateway(

) {
    fun sendEmail(to: String, letter: Letter) {
        println("Letter ${letter.name} was sent to $to")
        println("Letter: subject: ${letter.subject}, html: ${letter.getHtml()}")
    }
}