package com.khoinguyen.realworldapp.security;

import com.khoinguyen.realworldapp.entity.User;
import com.khoinguyen.realworldapp.model.TokenPayLoad;
import com.khoinguyen.realworldapp.repository.UserRepository;
import com.khoinguyen.realworldapp.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String token = null;
        TokenPayLoad tokenPayLoad = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Token")) {
            token = requestTokenHeader.substring(6).trim();
            try {
                tokenPayLoad = jwtTokenUtil.getTokenPayLoad(token);
            } catch (SignatureException e) {
                System.out.println("Invalid Jwt signature");
            }
            catch (IllegalArgumentException e) {
                System.out.println("Unable to get jwt");
            }
            catch (ExpiredJwtException e) {
                System.out.println("Token has expired");
            }
        } else {
            System.out.println("Jwt token does not start with 'Token'");
        }

        if(tokenPayLoad != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> userOptional = userRepository.findById(tokenPayLoad.getUserId());

            if(userOptional.isPresent()) {
                User user = userOptional.get();
                //check token hop le hay khong
                if(jwtTokenUtil.validate(token, user)) {
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                    UserDetails userDetails =
                            new org.springframework.security.core.userdetails.User(user.getEmail(),
                                    user.getPassword(),authorities);

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
