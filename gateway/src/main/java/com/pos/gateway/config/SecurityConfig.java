package com.pos.gateway.config;

import com.pos.gateway.config.jwt.JwtTokenVerifier;
import com.pos.gateway.config.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@EnableWebSecurity
public class SecurityConfig {

    public JwtUsernameAndPasswordAuthenticationFilter getJWTAuthenticationFilter(WebClient webClient) throws Exception {
        final JwtUsernameAndPasswordAuthenticationFilter filter = new JwtUsernameAndPasswordAuthenticationFilter(webClient);
        filter.setFilterProcessesUrl("/login");
        return filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, WebClient webClient) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session will not be persisted
            .and()
            .addFilter(getJWTAuthenticationFilter(webClient)) // adds the filter that validates credentials and generates jwt token
            .addFilterAfter(new JwtTokenVerifier(webClient), JwtUsernameAndPasswordAuthenticationFilter.class) // adds the filter that verifies the jwt token
            .authorizeHttpRequests(authorize ->
                    authorize.antMatchers("/login/**").permitAll()
                            .antMatchers("/logout/**").permitAll()
                            .antMatchers("/register/**").permitAll()
                            .antMatchers("/auth/**").hasRole("ADMIN")
                            .antMatchers("/users/**").hasRole("ADMIN")
                            .anyRequest().authenticated());

        return http.build();
    }
}