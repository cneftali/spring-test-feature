package fr.cneftali.spring.test.feature.conf;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class StandaloneInfrastructureNoDBConfiguration {

    // Permet d'enregistrer les/le jobs définis dans une classe javaconfig dans
    // le job registry au runtime (not dynamic)
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(
            final JobRegistry jobRegistry) throws Exception {
        final JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        jobRegistryBeanPostProcessor.afterPropertiesSet();
        return jobRegistryBeanPostProcessor;
    }
}
