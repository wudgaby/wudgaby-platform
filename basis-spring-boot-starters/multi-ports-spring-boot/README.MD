#### 提供前置条件处理. 如排除指定地址
```java
@Bean
public Predicate<String> excludedPathPredicate(){
    ExcludeRegistry excludeRegistry = ExcludeRegistry.ofStaticResource();
    Predicate<String> predicate = s -> PathHelper.excludedPath(excludeRegistry.getAllExcludePatterns(), s);
    return predicate;
}
```