# Spring Boot Starter for Retrofit
based on https://github.com/spring-cloud-incubator/spring-cloud-square

[Retrofit](http://square.github.io/retrofit/) turns your HTTP API into a Java interface.

This starter provides auto configuration for creating REST clients using Retrofit. Just create your 
interfaces and annotate them as `RetrofitClient`.
## Usage
#### Add dependency

```xml
<dependency>
    <groupId>com.mnazareno</groupId>
    <artifactId>retrofit-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### Create your interface

```java
@RetrofitClient(name = "github", baseUrl = "https://api.github.com")
public interface GithubApi {
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);
}
```
You can specify a configuration specific to a client if needed.
```java
@RetrofitClient(name = "fooApi", baseUrl = "https://api.foo.com", configuration = { FooConfiguration.class } )
public interface FooApi {
    @GET("/users")
    Call<List<User>> getUsers();
}

public class FooConfiguration {
    @Bean
    public OkHttpClient httpClient() {
    	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.company.com", 8080));
    	return new OkHttpClient.Builder().proxy(proxy).build();
    }
}
```
#### Ready to Use
Please refer to `retrofit-spring-boot-starter-demo` for sample usage 
```java
Response<List<Contributor>> response = githubApi.contributors("square", "retrofit").execute();
System.out.println(response);
```
