package com.wudgaby.retrofit.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
public class RetrofitAutoconfiguration {

	@Bean
	public RetrofitClientContext retrofitContext(
			Optional<List<RetrofitClientSpecification>> specs) {
		RetrofitClientContext retrofitClientContext = new RetrofitClientContext();
		specs.ifPresent(retrofitClientContext::setConfigurations);
		return retrofitClientContext;
	}

	@Configuration
	@EnableRetrofitClients
	class RetrofitClientConfiguration {
	}
}
