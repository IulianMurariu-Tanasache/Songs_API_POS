package com.pos.gateway.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.gateway.service.IdmConsumer;
import com.pos.gateway.soap.LoginRequest;
import com.pos.gateway.soap.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends
        UsernamePasswordAuthenticationFilter {

    private final IdmConsumer idmConsumer;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // get the credentials from the body of the post request
            LoginRequest loginRequest =
                    new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            LoginResponse loginResponse = null;
            try {
                loginResponse = idmConsumer.sendLoginRequest(loginRequest);
            } catch(NullPointerException e) {
                throw new BadCredentialsException("failed login");
            }

            System.out.println(loginResponse.getJwt());

            return new UsernamePasswordAuthenticationToken(
                    loginResponse.getId(),
                    loginResponse.getJwt(),
                    null
            ); // return result of authentication

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String token = (String) authResult.getCredentials();
        int id = (Integer) authResult.getPrincipal();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setJwt(token);
        loginResponse.setId(id);
        response.setHeader("Content-Type","application/json");
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        response.setStatus(401);
    }
}