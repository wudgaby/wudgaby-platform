package com.wudgaby.retrofit.springboot.demo.github;

import com.wudgaby.retrofit.springboot.RetrofitClient;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;


@RetrofitClient(name = "github", baseUrl = "https://api.github.com")
public interface GithubApi {

	@GET("/repos/{owner}/{repo}/contributors")
	Call<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);
}
