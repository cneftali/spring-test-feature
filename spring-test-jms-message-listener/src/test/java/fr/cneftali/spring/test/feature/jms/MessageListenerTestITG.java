package fr.cneftali.spring.test.feature.jms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cneftali.spring.test.feature.jms.conf.JmsContext;
import fr.cneftali.spring.test.feature.jms.entity.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JmsContext.class)
public class MessageListenerTestITG {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Test
	public void whould_work() {
		
		for (long cpt = 0; cpt <= 10000; cpt ++) {
			jmsTemplate.convertAndSend(new Request(cpt, "tototo"));
		}
	}
}
