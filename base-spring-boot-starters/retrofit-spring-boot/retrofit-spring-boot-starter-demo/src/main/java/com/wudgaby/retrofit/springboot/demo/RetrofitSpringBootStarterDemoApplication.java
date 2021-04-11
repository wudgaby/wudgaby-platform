package com.wudgaby.retrofit.springboot.demo;

import com.wudgaby.retrofit.springboot.demo.github.Contributor;
import com.wudgaby.retrofit.springboot.demo.github.GithubApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.util.List;

@SpringBootApplication
public class RetrofitSpringBootStarterDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetrofitSpringBootStarterDemoApplication.class, args);
	}

	@Component
	class RetrofitSampleRunner implements CommandLineRunner {

		@Autowired
		private GithubApi githubApi;

		@Override
		public void run(String... args) throws Exception {
			Response<List<Contributor>> response = this.githubApi
					.contributors("square", "retrofit").execute();
			System.out.println(response);
			System.out.println(response.headers());
			System.out.println(response.body());
		}
	}
}
