package su.arlet.business1.configs

import jakarta.jms.ConnectionFactory
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ActiveMQConfig {

    @Value("\${spring.artemis.broker-url}")
    private lateinit var brokerUrl: String

    @Value("\${spring.artemis.user}")
    private lateinit var user: String

    @Value("\${spring.artemis.password}")
    private lateinit var password: String

    @Bean
    fun connectionFactory(): ConnectionFactory {
        return ActiveMQConnectionFactory("tcp://localhost:5672?protocols=STOMP,AMQP,MQTT", user, password)
    }
}
