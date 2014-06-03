package fr.cneftali.spring.test.feature.jms.conf;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

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
    @Bean(destroyMethod = "destroy")
    public CachingConnectionFactory connectionFactory() {
        final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(amqPoolConnectionFactory());
        cachingConnectionFactory.setSessionCacheSize(100);
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

    @Bean
    public MessageListener messageListener() {
        final MessageListener messageListener = new MessageListener() {
            private final Logger LOGGER = LoggerFactory.getLogger("messageListener");

            @Override
            public void onMessage(final Message message) {
                if (message instanceof ObjectMessage) {
                    try {
                        LOGGER.info(((ObjectMessage) message).getObject().toString());
                    } catch (final JMSException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        return messageListener;
    }

    @Bean(destroyMethod = "destroy")
    public DefaultMessageListenerContainer jmsContainer() {
        final DefaultMessageListenerContainer jmsContainer = new DefaultMessageListenerContainer();
        jmsContainer.setDestination(destination());
        jmsContainer.setConnectionFactory(connectionFactory());
        jmsContainer.setMessageListener(messageListener());
        jmsContainer.setConcurrency("5-10");
        jmsContainer.setReceiveTimeout(5000);
        jmsContainer.afterPropertiesSet();
        return jmsContainer;
    }
}
