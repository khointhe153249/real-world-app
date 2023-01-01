package com.khoinguyen.realworldapp.util;

import com.khoinguyen.realworldapp.entity.User;
import com.khoinguyen.realworldapp.model.TokenPayLoad;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private String SECRET = "KHOI_NT4";

    public String generateToken(User user, long expiredDate) {
        Map<String, Object> claims = new HashMap<>();
        TokenPayLoad tokenPayLoad = TokenPayLoad.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();
        claims.put("payload", tokenPayLoad);
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredDate*1000))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public TokenPayLoad getTokenPayLoad(String token) {
        return getClaimsFromToken(token, claims -> {
            Map<String, Object> mapResult = (Map<String, Object>) claims.get("payload");
            return TokenPayLoad.builder()
                    .userId((Integer) mapResult.get("userId"))
                    .email((String) mapResult.get("email"))
                    .build();
        });
    }

    private<T> T getClaimsFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claimResolver.apply(claims);
    }

    public boolean validate(String token, User user) {
        TokenPayLoad tokenPayLoad = getTokenPayLoad(token);

        return tokenPayLoad.getUserId() == user.getId()
                && tokenPayLoad.getEmail().equals(user.getEmail())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiredDate = getClaimsFromToken(token, Claims::getExpiration);
        return expiredDate.before(new Date());
    }
}
