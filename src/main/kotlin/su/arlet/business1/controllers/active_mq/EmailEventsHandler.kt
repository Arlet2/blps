package su.arlet.business1.controllers.active_mq

import org.springframework.context.annotation.Profile
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import su.arlet.business1.gateways.email.EmailInfo
import su.arlet.business1.services.EmailService

@Component
class EmailEventsHandler (
    val emailService: EmailService,
) {

    @JmsListener(destination = "test-queue")
    fun receiveEvent(emailInfo: EmailInfo) {
        emailService.sendEmail(emailInfo)
    }
}