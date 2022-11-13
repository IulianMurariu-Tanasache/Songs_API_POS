package com.pos.gateway.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.commons.idm.AuthenticationRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends
        UsernamePasswordAuthenticationFilter {

    private final WebClient webClient;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // get the credentials from the body of the post request
            AuthenticationRequest authenticationRequest =
                    new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequest.class);

            String jwt = webClient.post()
                    .uri("http://localhost:8001/authenticate")
                    .bodyValue(authenticationRequest)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, (clientResponse) -> Mono.just(new BadCredentialsException("Bad credentials")))
                    .bodyToMono(String.class)
                    .block(); // TODO: add timeout?

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    jwt,
                    null
            );

            return authentication; // return result of authentication

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String token = (String) authResult.getCredentials();
        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        response.setStatus(401);
    }
}