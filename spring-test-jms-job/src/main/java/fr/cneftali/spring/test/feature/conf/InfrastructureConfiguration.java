package fr.cneftali.spring.test.feature.conf;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;

public interface InfrastructureConfiguration {
 
	@Bean
	public DataSource dataSource();
 
}