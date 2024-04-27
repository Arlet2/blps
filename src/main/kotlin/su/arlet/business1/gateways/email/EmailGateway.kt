package su.arlet.business1.gateways.email

import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import su.arlet.business1.core.EmailInfo
import su.arlet.business1.core.letters.Letter

@Service
class EmailGateway (
    private val template: JmsTemplate,
) {
    fun sendEmail(to: String, letter: Letter) {
        template.convertAndSend("test-queue", EmailInfo(to, letter))
    }
}