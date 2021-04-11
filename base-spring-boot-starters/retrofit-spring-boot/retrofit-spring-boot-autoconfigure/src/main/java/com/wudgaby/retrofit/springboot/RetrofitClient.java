package com.wudgaby.retrofit.springboot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface RetrofitClient {
	String name();

	Class[] configuration() default {};

	String baseUrl();
}
