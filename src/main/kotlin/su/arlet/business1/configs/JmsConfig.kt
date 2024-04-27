package su.arlet.business1.configs

import jakarta.jms.ConnectionFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import su.arlet.business1.gateways.email.letters.Letter


@EnableJms
@Configuration
class JmsConfig {
    @Bean
    fun jmsTemplate(@Qualifier("jmsConnectionFactory") connectionFactory: ConnectionFactory): JmsTemplate {
        val jmsTemplate = JmsTemplate(connectionFactory)
        jmsTemplate.messageConverter = messageConverter()
        return jmsTemplate
    }

    @Bean
    fun messageConverter(): MessageConverter {
        val converter = MappingJackson2MessageConverter()

        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")

        return converter
    }
}