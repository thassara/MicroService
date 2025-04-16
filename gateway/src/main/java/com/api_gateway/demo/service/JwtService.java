package com.api_gateway.demo.service;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import static io.jsonwebtoken.Jwts.*;

@Component
public class JwtService {

    private final String SECRET_KEY = "mySuperSecretKeyThatIsLongEnough123456";

    // Validate token
    public void validateToken(String token) {
        parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token); // Throws error if invalid
    }

    // Extract the role from the token
    public String extractRole(String token) {
        Claims claims = parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();


        return claims.get("role", String.class);
    }

    public String extractUsername(String token) {
        return parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

