package su.arlet.business1.gateways.email

import jakarta.annotation.PostConstruct
import jakarta.jms.ConnectionFactory
import jakarta.jms.Destination
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import su.arlet.business1.gateways.email.letters.Letter
import su.arlet.business1.gateways.email.letters.TestLetter
import kotlin.reflect.jvm.jvmName

@Service
class EmailGateway (
    private val template: JmsTemplate,
) {
    fun sendEmail(to: String, letter: Letter) {
        template.convertAndSend("test-queue", EmailInfo(to, letter))
    }
}