package fr.cneftali.spring.test.feature.jms.job.items;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import fr.cneftali.spring.test.feature.jms.entity.Request;

@Component
public class LogItemWriter implements ItemWriter<Request> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogItemWriter.class);

    @Override
    public void write(List<? extends Request> items) throws Exception {
        for (final Request request : items) {
            LOGGER.info("**** [LogItemWriter] writing item: " + request);
        }
    }
}
