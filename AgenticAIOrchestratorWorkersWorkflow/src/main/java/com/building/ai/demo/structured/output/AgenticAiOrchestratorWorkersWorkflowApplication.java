package com.building.ai.demo.structured.output;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@SpringBootApplication
public class AgenticAiOrchestratorWorkersWorkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgenticAiOrchestratorWorkersWorkflowApplication.class, args);
	}

	 // Enables logging of all outgoing HTTP requests made to the LLM API through Logbook
    @Bean
    public RestClientCustomizer restClientCustomizer(Logbook logbook) {
        return restClientBuilder -> restClientBuilder.requestInterceptor(new LogbookClientHttpRequestInterceptor(logbook));
    }
}
