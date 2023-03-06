package com.example.dripchip.filters;

import com.example.dripchip.security.AccountDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    private final AccountDetailsServiceImpl service;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public HeaderAuthenticationFilter(AccountDetailsServiceImpl service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String auth = request.getHeader("Authorization");

        UserDetails user = findByToken(auth);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid token");
        } else {
            final UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        }
    }

    private UserDetails findByToken(String token) {
        System.out.println(token);//Basic <Base64 encoded username and password>

        token = token.substring(6);

        System.out.println(token);//<Base64 encoded username and password>

        byte[] decodedBytes = Base64.getDecoder().decode(token.getBytes());
        String decodedString = new String(decodedBytes);
        String[] emailAndPassword = decodedString.split(":");

        try {
            UserDetails user = service.loadUserByUsername(emailAndPassword[0]);
            if (passwordEncoder.matches(emailAndPassword[1], user.getPassword())) {
                return user;
            } else return null;
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }
}
