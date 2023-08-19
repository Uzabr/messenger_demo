package com.example.security;

import com.example.entity.User;
import com.example.service.CustomerUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private  JWTTokenProvider provider;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CustomerUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJWTFromRequest(request);
            if (StringUtils.hasText(jwt) && provider.validateToken(jwt)) {
                Long userId = provider.getUserIdFromToken(jwt);
                User userDetails = userDetailService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.emptyList());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        catch (Exception ex) {
            LOG.error("Could not set user authentication");
        }

        filterChain.doFilter(request, response);

    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String beartoken = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(beartoken) && beartoken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return beartoken.split(" ")[1];
        }
        return null;
    }
}
