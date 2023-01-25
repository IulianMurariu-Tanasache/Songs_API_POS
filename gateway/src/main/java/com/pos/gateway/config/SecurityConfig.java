package com.pos.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.gateway.security.jwt.JwtTokenVerifier;
import com.pos.gateway.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.pos.gateway.service.IdmConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    public JwtUsernameAndPasswordAuthenticationFilter getJWTAuthenticationFilter(IdmConsumer consumer, ObjectMapper objectMapper) throws Exception {
        final JwtUsernameAndPasswordAuthenticationFilter filter = new JwtUsernameAndPasswordAuthenticationFilter(consumer, objectMapper);
        filter.setFilterProcessesUrl("/login");
        return filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, IdmConsumer idmConsumer, ObjectMapper objectMapper) throws Exception {
        http.csrf().disable()
            .cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session will not be persisted
            .and()
            .addFilter(getJWTAuthenticationFilter(idmConsumer, objectMapper)) // adds the filter that validates credentials and generates jwt token
            .addFilterAfter(new JwtTokenVerifier(idmConsumer), JwtUsernameAndPasswordAuthenticationFilter.class) // adds the filter that verifies the jwt token
            .authorizeHttpRequests(authorize ->
                    authorize.antMatchers("/login/**").permitAll()
                            .antMatchers("/register/**").permitAll()
                            .antMatchers("/actuator/prometheus").permitAll()
                            .anyRequest().authenticated());

        return http.build();
    }
}