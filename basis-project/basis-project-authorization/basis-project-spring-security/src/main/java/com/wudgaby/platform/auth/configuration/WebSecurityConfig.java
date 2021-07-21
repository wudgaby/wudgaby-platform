package com.wudgaby.platform.auth.configuration;

import com.wudgaby.platform.auth.annotation.AnonymousAccess;
import com.wudgaby.platform.auth.extend.authway.email.EmailAuthenticationSecurityConfig;
import com.wudgaby.platform.auth.extend.authway.sms.SmsCodeAuthenticationSecurityConfig;
import com.wudgaby.platform.auth.extend.authway.username.UserNameAuthenticationProvider;
import com.wudgaby.platform.auth.extend.authway.username.UserNameAuthenticationSecurityConfig;
import com.wudgaby.platform.auth.extend.entrypoint.RestAuthenticationEntryPoint;
import com.wudgaby.platform.auth.extend.handler.AuthFailureHandler;
import com.wudgaby.platform.auth.extend.handler.AuthSuccessHandler;
import com.wudgaby.platform.auth.extend.handler.MyLogoutSuccessHandler;
import com.wudgaby.platform.auth.extend.handler.RestAuthAccessDeniedHandler;
import com.wudgaby.platform.auth.extend.strategy.MyExpiredSessionStrategy;
import com.wudgaby.platform.auth.extend.strategy.MyInvalidSessionStrategy;
import com.wudgaby.platform.core.config.ExcludeRegistry;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName : WebSecurityConfig
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/3 14:21
 * @Desc :
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(value = "security.jwt.enabled", havingValue = "false", matchIfMissing = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired private AuthSuccessHandler authSuccessHandler;
    @Autowired private AuthFailureHandler authFailureHandler;
    @Autowired private MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Autowired private RestAuthAccessDeniedHandler restAuthAccessDeniedHandler;
    @Autowired(required = false) private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired(required = false) private EmailAuthenticationSecurityConfig emailAuthenticationSecurityConfig;
    @Autowired(required = false) private UserNameAuthenticationSecurityConfig userNameAuthenticationSecurityConfig;
    @Autowired private RestAuthenticationEntryPoint jsonAuthenticationEntryPoint;
    @Autowired private RememberMeServices rememberMeServices;
    @Autowired private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
    @Autowired private AccessDecisionManager accessDecisionManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ApplicationContext applicationContext;
    @Autowired private CorsFilter corsFilter;

    @Resource(name = "accountUserService")
    private UserDetailsService userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /*@Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 去除 ROLE_ 前缀
        return new GrantedAuthorityDefaults("");
    }*/

    @Bean
    public AuthenticationProvider userNameAuthenticationProvider(){
        UserNameAuthenticationProvider authenticationProvider = new UserNameAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    /*@Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }*/

    /**
     * AuthenticationManagerBuilder 232行代码
     * No authenticationProviders and no parentAuthenticationManager defined. Returning null. 导致authenticationManagerBuilder.getObject() 为 null
     * @param auth
     * @throws Exception
     */
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //必须设置一个.否则authenticationManager.authenticate(token)会出现死循环和 authenticationManagerBuilder.getObject() 为 null
        auth.authenticationProvider(userNameAuthenticationProvider());
        super.configure(auth);
    }*/

    /**
     * 添加 AuthenticationManager 注解后，出现 java.lang.StackOverflowError: null
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userNameAuthenticationProvider());
    }

    /**
     * 允许URL出现//
     * @return
     */
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        /*StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;*/
        return new DefaultHttpFirewall();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());

        ExcludeRegistry excludeRegistry = new ExcludeRegistry();
        excludeRegistry.excludePathPatterns("/*.js", "/*.html", "/*.htm", "/*.css", "/*.txt",
                "/public/**", "/static/**", "/**/*.js", "/**/*.ico");
        excludeRegistry.excludePathPatterns("/auth/**");

        Set<String> anonymousUrls = getAnonymousUrls();
        anonymousUrls.addAll(excludeRegistry.getAllExcludePatterns());
        web.ignoring().antMatchers(anonymousUrls.toArray(new String[0]));
    }

    /**
     * 搜寻匿名标记 url： @AnonymousAccess
     */
    private Set<String> getAnonymousUrls(){
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (null != anonymousAccess) {
                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns().stream()
                        .map(urlPattern -> RegExUtils.replacePattern(urlPattern, "\\{\\w+\\}", "{*}"))
                        .collect(Collectors.toSet()));
            }
        }
        return anonymousUrls;
    }


    /**
     * 自定义 FilterSecurityInterceptor  ObjectPostProcessor 以替换默认配置达到动态权限的目的
     *
     * @return ObjectPostProcessor
     */
    private ObjectPostProcessor<FilterSecurityInterceptor> filterSecurityInterceptorObjectPostProcessor() {
        return new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                object.setAccessDecisionManager(accessDecisionManager);
                object.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
                return object;
            }
        };
    }

    /*@Bean
    public List<RequestMatcher> ignoringPatterns(){
        List<String> antPatterns = Lists.newArrayList("/", "/login", "/emailLogin", "/mobileLogin", "/ajaxLogin", "/favicon.ico");
        antPatterns.addAll(Lists.newArrayList("/v2/api-docs",
                "/swagger-resources/configuration/ui",
                "/swagger-resources",
                "/swagger-resources/configuration/security",
                "/swagger-ui.html");
        antPatterns.addAll(Lists.newArrayList("/auth/**", "/social.html"));

        List<RequestMatcher> matchers = new ArrayList<>();
        for (String pattern : antPatterns) {
            matchers.add(new AntPathRequestMatcher(pattern, null));
        }
        matchers.add(new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.toString()));
        return matchers;
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //用DynamicallyUrlInterceptor取代这里
                //.antMatchers("/login", "/emailLogin", "/mobileLogin").permitAll()
                //.antMatchers("/**/favicon.ico").permitAll()
                //.antMatchers("/v2/api-docs",
                //        "/swagger-resources/configuration/ui",
                //        "/swagger-resources",
                //        "/swagger-resources/configuration/security",
                //        "/swagger-ui.html",
                //        "/webjars/**").permitAll()
                //.antMatchers("/auth/**", "/social.html").permitAll()
                //.antMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated()
                .withObjectPostProcessor(filterSecurityInterceptorObjectPostProcessor())
                .and()
                    .formLogin()
                    //.loginPage("/login")
                    //.loginPage("http://www.baidu.com")
                    //loginProcessingUrl用于指定前后端分离的时候调用后台登录接口的名称
                    .loginProcessingUrl("/login")
                    //配置登录成功的自定义处理类
                    .successHandler(authSuccessHandler)
                    //配置登录失败的自定义处理类
                    .failureHandler(authFailureHandler)
                    //.successForwardUrl("/auth/success")
                    //.failureForwardUrl("/auth/failure")
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(myLogoutSuccessHandler)
                .and()
                    .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                    .apply(emailAuthenticationSecurityConfig)
                .and()
                    .apply(userNameAuthenticationSecurityConfig)
                .and()
                    .exceptionHandling()
                    //配置没有权限的自定义处理类
                    .accessDeniedHandler(restAuthAccessDeniedHandler)
                    //配置未认证的自定义处理类, 不配置并且配置了formlogin()时返回的是登录页
                    .authenticationEntryPoint(jsonAuthenticationEntryPoint)
                .and()
                    .rememberMe()
                    .rememberMeServices(rememberMeServices)
                .and()
                    .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                    .headers()
                    .frameOptions().sameOrigin()
                    .and()
                    .cors()
                .and()
                    .csrf().disable()// 取消跨站请求伪造防护
                .sessionManagement()
                    //session 创建策略  jwt时.永远不创建session
                    //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .invalidSessionStrategy(new MyInvalidSessionStrategy())
                    .maximumSessions(1)
                    //是否保留已经登录的用户；为true，新用户无法登录；为 false，旧用户被踢出
                    .maxSessionsPreventsLogin(true)
                    .expiredSessionStrategy(new MyExpiredSessionStrategy())
                    .sessionRegistry(sessionRegistry())
                ;
    }

    /**
     * 设置session注册器
     * @return
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * SpringSecurity内置的session监听器
     * @return
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
