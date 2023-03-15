package com.example.dripchip.filters;

import com.example.dripchip.exceptions.PasswordMismatchException;
import com.example.dripchip.security.AccountDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    private final AccountDetailsServiceImpl service;
    private final PasswordEncoder passwordEncoder;

    public HeaderAuthenticationFilter(AccountDetailsServiceImpl service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("==========================\n" + request.getRequestURI() +
                "==========================");
        String authToken = request.getHeader("Authorization");

        if (authToken == null && request.getRequestURI().equals("/registration")) {
            System.out.println("auth null registration");
            System.out.println("filter exit");
            filterChain.doFilter(request, response);
        } else if (authToken == null) { //todo ---------------
            filterChain.doFilter(request, response);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token not found");
        } else {
            System.out.println("auth not null loading user");


            try {

                String[] credentials = parseToken(authToken);

                UserDetails user = service.loadUserByUsername(credentials[0]);
                if (!passwordEncoder.matches(credentials[1], user.getPassword())) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "password mismatch");
                }

                final UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("logged user = " + user.getUsername());
                System.out.println("filter exit");

                filterChain.doFilter(request, response);
            } catch (Exception e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "user not found");
            }
        }
    }

    private String[] parseToken(String token) {
        System.out.println("token = " + token);//Basic <Base64 encoded username and password>
        token = token.substring(6);

        byte[] decodedBytes = Base64.getDecoder().decode(token.getBytes());
        String decodedString = new String(decodedBytes);
        String[] emailAndPassword = decodedString.split(":");

        System.out.println("email = " + emailAndPassword[0]);
        System.out.println("pass = " + emailAndPassword[1]);

        return emailAndPassword;
    }
}
