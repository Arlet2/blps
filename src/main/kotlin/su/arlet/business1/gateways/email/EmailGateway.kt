package su.arlet.business1.gateways.email

import EmailInfo
import letters.Letter
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service

@Service
class EmailGateway(
    private val template: JmsTemplate,
) {
    private val queueName = "email-queue"
    fun sendEmail(to: String, letter: Letter) {
        template.convertAndSend(queueName, EmailInfo(to, letter))
    }
}