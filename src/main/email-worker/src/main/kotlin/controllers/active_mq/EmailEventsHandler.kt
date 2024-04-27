package su.arlet.controllers.active_mq

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import su.arlet.core.EmailInfo
import su.arlet.services.EmailService

@Component
class EmailEventsHandler (
    val emailService: EmailService,
) {

    @JmsListener(destination = "test-queue")
    fun receiveEvent(emailInfo: EmailInfo) {
        emailService.sendEmail(emailInfo)
    }
}