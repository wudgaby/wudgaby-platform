package com.wudgaby.oauth2.uaa.config;

import com.wudgaby.oauth2.uaa.enhance.mobile.MobileAuthenticationFilter;
import com.wudgaby.oauth2.uaa.enhance.mobile.MobileAuthenticationProvider;
import com.wudgaby.oauth2.uaa.service.MobileUserServiceDetail;
import com.wudgaby.oauth2.uaa.service.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @ClassName : WebSecurityConfig
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/23 17:27
 * @Desc :
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceDetail userServiceDetail;

    @Autowired
    private MobileUserServiceDetail mobileUserServiceDetail;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //加入图片验证码过滤器
        //CaptchaFilter captchaFilter = new CaptchaFilter();
        //captchaFilter.setFailureHandler(tigerAuthenticationFailureHandler);

        http
                //.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(mobileAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/token")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()

                /*.successHandler(new MyLoginAuthSuccessHandler())
                .failureHandler(new CustomerAuthenticationFailureHandler())
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessHandler(new AuthLogoutSuccessHandler())*/

                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userServiceDetail).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(daoAuthenticationProvider());
        auth.authenticationProvider(mobileAuthenticationProvider());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider1 = new DaoAuthenticationProvider();
        // 设置userDetailsService
        provider1.setUserDetailsService(userServiceDetail);
        // 禁止隐藏用户未找到异常
        provider1.setHideUserNotFoundExceptions(false);
        // 使用BCrypt进行密码的hash
        provider1.setPasswordEncoder(passwordEncoder());
        return provider1;
    }

    @Bean
    public MobileAuthenticationProvider mobileAuthenticationProvider(){
        MobileAuthenticationProvider provider = new MobileAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(mobileUserServiceDetail);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    /**
     * 手机验证码登陆过滤器
     * @return
     */
    @Bean
    public MobileAuthenticationFilter mobileAuthenticationFilter() {
        MobileAuthenticationFilter filter = new MobileAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //filter.setAuthenticationSuccessHandler(new MobileLoginSuccessHandler());
        //filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"));
        return filter;
    }

}
