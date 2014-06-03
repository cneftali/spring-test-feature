package fr.cneftali.spring.test.feature.conf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cneftali.spring.test.feature.jms.entity.Request;
import fr.cneftali.spring.test.feature.jms.job.conf.Job1Config;

@ContextConfiguration(classes = { StandaloneInfrastructureConfiguration.class, Job1Config.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class StandaloneInfrastructureConfigurationTest {

	@Autowired
	private JobRegistry jobRegistry;
 
	@Autowired
	private JobLauncher jobLauncher;
 
//	@Autowired
//	private DataSource dataSource;
	
	@Autowired
	private JmsTemplate jmsTemplate;
 
//	private JdbcTemplate jdbcTemplate;
 
	@Before
	public void setup(){
//		jdbcTemplate = new JdbcTemplate(dataSource);
		for (long cpt = 0; cpt < 50000 ; cpt++) {
			jmsTemplate.convertAndSend(new Request(cpt, "test" + cpt));
		}
	}
 
	@Test
	public void testLaunchJob() throws Exception {
		Job job = jobRegistry.getJob("jmsToLog");
		jobLauncher.run(job, new JobParameters());
	}
}
