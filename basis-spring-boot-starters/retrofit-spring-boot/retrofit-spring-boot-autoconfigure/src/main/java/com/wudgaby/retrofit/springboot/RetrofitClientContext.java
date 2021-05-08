package com.wudgaby.retrofit.springboot;

import org.springframework.cloud.context.named.NamedContextFactory;

public class RetrofitClientContext extends NamedContextFactory<RetrofitClientSpecification> {
	public RetrofitClientContext() {
		super(DefaultRetrofitClientConfiguration.class, "retrofit", "retrofit.client.name");
	}
}
