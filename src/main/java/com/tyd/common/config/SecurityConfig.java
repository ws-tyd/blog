package com.tyd.common.config;

import com.tyd.common.filter.AuthenticationEntryPointHandler;
import com.tyd.common.filter.MyAccessDeniedHandler;
import com.tyd.common.filter.RequestCountFilter;
import com.tyd.common.filter.SimpleTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全配置
 *
 * @author 谭越东
 * @date 2022/09/27
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String RELEASEROUTE = "/,/test,/login,/swagger-ui.html,/webjars/**,/swagger-resources/**," +
            "/v2/**,/register/**,/image/**,/product/file/**,/task/**,/task,/sse/**,/sse,/search/**";

    @Autowired
    private AuthenticationEntryPointHandler authenticationEntryPointHandler;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private SimpleTokenFilter simpleTokenFilter;

    @Autowired
    private RequestCountFilter requestCountFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers(RELEASEROUTE.split(","))
                .permitAll()
                .anyRequest()
                .authenticated();
        http.logout().disable();
        //错误处理
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(myAccessDeniedHandler);
        http.addFilterBefore( requestCountFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(simpleTokenFilter , UsernamePasswordAuthenticationFilter.class);

    }
}
