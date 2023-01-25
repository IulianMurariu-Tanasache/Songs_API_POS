package com.pos.gateway.security.jwt;

import com.pos.gateway.service.IdmConsumer;
import com.pos.gateway.soap.AuthRequest;
import com.pos.gateway.soap.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final IdmConsumer idmConsumer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization"); // get the token from the headers
        if(authorizationHeader == null || !(authorizationHeader.startsWith("Bearer "))) {
            filterChain.doFilter(request,response); // skip this filter if no token
            return;
        }

        try{
            AuthRequest authRequest = new AuthRequest();
            authRequest.setJwt(authorizationHeader.replaceAll("Bearer ", ""));
            AuthResponse authResponse = idmConsumer.sendAuthRequest(authRequest);

            System.out.println(authResponse.getId());
            System.out.println(authResponse.getRoles());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authResponse.getId(),
                    null,
                    authResponse.getRoles().stream()
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