package fr.cneftali.spring.test.feature.jms;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cneftali.spring.test.feature.jms.conf.JmsContext;
import fr.cneftali.spring.test.feature.jms.entity.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JmsContext.class })
@DirtiesContext
public class MessageListenerTestITG {

    @Autowired
    private JmsTemplate jmsTemplate;

    @AfterClass
    public static void afterClass() throws Exception {
        Thread.sleep(1000);
    }

    @Test
    public void whould_work() {
        jmsTemplate.convertAndSend(new Request(1L, "tototo"));
    }
}
