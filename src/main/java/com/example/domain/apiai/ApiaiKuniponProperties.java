package com.example.domain.apiai;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/properties/apiai/kunipon-intents.properties")
public class ApiaiKuniponProperties {

	private final Environment env;
	
	public ApiaiKuniponProperties(Environment env) {
		this.env = env;
	}
	
	public String getSpeech(String fullfilmentAction) {
		return env.getProperty(fullfilmentAction);
	}
}
