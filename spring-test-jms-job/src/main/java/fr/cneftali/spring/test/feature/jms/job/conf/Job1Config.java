package fr.cneftali.spring.test.feature.jms.job.conf;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

import fr.cneftali.spring.test.feature.conf.InfrastructureConfiguration;
import fr.cneftali.spring.test.feature.jms.entity.Request;

@Configuration
@Import(value = { JmsContext.class })
@ComponentScan(basePackages = { "fr.cneftali.spring.test.feature.jms.job.items" })
public class Job1Config {

	@Autowired
	private JobBuilderFactory jobBuilders;
 
	@Autowired
	private StepBuilderFactory stepBuilders;
 
	@Autowired
	private InfrastructureConfiguration infrastructureConfiguration;
	
	@Bean
	public JmsItemReader<Request> itemReader(final JmsTemplate jmsTemplate) throws Exception {
		final JmsItemReader<Request> itemReader = new JmsItemReader<>();
		itemReader.setJmsTemplate(jmsTemplate);
		itemReader.afterPropertiesSet();
		return itemReader;
	}
	
	@Bean
	public Step step(final ItemWriter<Request> writer) throws Exception {
		return stepBuilders.get("step")
				.<Request, Request>chunk(1)
				.reader(itemReader(null))
				.writer(writer)
				.build();
	}
	
	@Bean
	public Job jmsToLog() throws Exception {
		return jobBuilders.get("jmsToLog")
				.start(step(null))
				.build();
	}
}
