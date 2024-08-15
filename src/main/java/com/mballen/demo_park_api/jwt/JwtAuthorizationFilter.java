package com.mballen.demo_park_api.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

   
    private JwtUserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
         
                final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);
                if(token == null || !token.startsWith(JwtUtils.JWT_BEARER)){
                    log.info("JWT token está nulo, vazio ou não inciado com 'Bearer'.");
                    filterChain.doFilter(request, response);
                    return;
                }

                if(!JwtUtils.isTokenValid(token)){
                    log.warn("JWT token está inválido ou expirado");
                    filterChain.doFilter(request, response);
                    return;
                }

                String username = JwtUtils.getUsernameFromToken(token);

                toAuthentication(request, username);

                filterChain.doFilter(request, response);

    }

    private void toAuthentication(HttpServletRequest request, String username) {
        
        UserDetails userDetails = detailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    
}
