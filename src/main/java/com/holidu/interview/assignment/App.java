package com.holidu.interview.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

@SpringBootApplication
public class App {

	private static final String APP_TOKEN = "AZArqBtddw8vDLzUrDc4iyxxX";

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.interceptors(Collections.singleton(new HeaderRequestInterceptor("X-App-Token", APP_TOKEN)))
				.build();
	}

	public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

		private final String headerName;

		private final String headerValue;

		HeaderRequestInterceptor(String headerName, String headerValue) {
			this.headerName = headerName;
			this.headerValue = headerValue;
		}

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws
				IOException {
			request.getHeaders().set(headerName, headerValue);
			return execution.execute(request, body);
		}
	}
}
