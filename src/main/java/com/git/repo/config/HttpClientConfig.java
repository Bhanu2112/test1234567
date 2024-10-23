package com.git.repo.config;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {
	
	@Bean
	public HttpClient client() {
		return HttpClient.newHttpClient();
	}

}
