package com.building.ai.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@SpringBootApplication
public class AgenticAiParallelizationWorkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgenticAiParallelizationWorkflowApplication.class, args);
	}

	 @Bean
	    public RestClientCustomizer restClientCustomizer(Logbook logbook) {
	        return restClientBuilder -> restClientBuilder.requestInterceptor(new LogbookClientHttpRequestInterceptor(logbook));
	    }

}
