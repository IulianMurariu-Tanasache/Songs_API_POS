package com.pos.gateway.config.jwt;

import com.pos.commons.idm.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final WebClient webClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization"); // get the token from the headers
        if(authorizationHeader == null || !(authorizationHeader.startsWith("Bearer "))) {
            filterChain.doFilter(request,response); // skip this filter if no token
            return;
        }

        try{
            UserDTO authenticationResponse = webClient.post()
                    .uri("http://localhost:8001/verify")
                    .bodyValue(authorizationHeader)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationResponse.getUsername(),
                    null,
                    authenticationResponse.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            response.setStatus(403);
        }

        filterChain.doFilter(request, response);
    }
}