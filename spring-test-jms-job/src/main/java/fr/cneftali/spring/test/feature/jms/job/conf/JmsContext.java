package fr.cneftali.spring.test.feature.jms.job.conf;

import java.io.IOException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsContext {

	@Bean(initMethod = "start", destroyMethod = "stop")
	public BrokerService brokerService() throws IOException {
		final BrokerService brokerService = new BrokerService();
		brokerService.setBrokerName("localhost");
		brokerService.setUseJmx(true);
		brokerService.setTransportConnectorURIs(new String [] { "vm://localhost" });
		brokerService.setPersistenceAdapter(new MemoryPersistenceAdapter());
		return brokerService;
	}
	
	@Bean
	@DependsOn("brokerService")
	public ActiveMQConnectionFactory amqConnectionFactory() {		
		return new ActiveMQConnectionFactory("vm://localhost");
	}
	
	@Bean
	public PooledConnectionFactory amqPoolConnectionFactory() {
		return new PooledConnectionFactory(amqConnectionFactory());
	}
	
	// A cached connection to wrap the ActiveMQ connection
	@Bean
	public CachingConnectionFactory connectionFactory() {
		 final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(amqPoolConnectionFactory());
		 cachingConnectionFactory.setSessionCacheSize(100);
		 cachingConnectionFactory.setCacheProducers(true);
		 cachingConnectionFactory.setCacheConsumers(true);
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

