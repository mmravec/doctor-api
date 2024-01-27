package com.bunch.of.ideas.doctorapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
public class SecurityConfig {
    // https://www.baeldung.com/spring-security-5-oauth2-login
    // https://stackoverflow.com/questions/74753700/cannot-resolve-method-antmatchers-in-authorizationmanagerrequestmatcherregis

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/oauth_login","/v1/customers/search").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(form -> form
                        .loginPage("/oauth_login")
                        .defaultSuccessUrl("/loginSuccess",true)
                        .failureUrl("/loginFailure")
                )
                .logout(out->out
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/oauth_login")
                )
                .csrf(c->c
                        .csrfTokenRepository(csrfTokenRepository())
                );

        return http.build();
    }
}
