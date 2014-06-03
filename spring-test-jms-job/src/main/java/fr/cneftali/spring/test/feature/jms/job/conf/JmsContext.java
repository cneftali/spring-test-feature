package fr.cneftali.spring.test.feature.jms.job.conf;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsContext {

    @Bean
    public ActiveMQConnectionFactory amqConnectionFactory() {
        return new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
    }

    @Bean
    public PooledConnectionFactory amqPoolConnectionFactory() {
        return new PooledConnectionFactory(amqConnectionFactory());
    }

    // A cached connection to wrap the ActiveMQ connection
    @Bean
    public CachingConnectionFactory connectionFactory() {
        final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(
                amqPoolConnectionFactory());
        cachingConnectionFactory.setSessionCacheSize(100);
        cachingConnectionFactory.setCacheProducers(true);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean
    public ActiveMQQueue destination() {
        return new ActiveMQQueue("test-queue");
    }

    @Bean
    @Lazy
    public JmsTemplate jmsTemplate() {
        final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setDefaultDestination(destination());
        jmsTemplate.setReceiveTimeout(1000);
        return jmsTemplate;
    }
}
