package org.financetracker.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.financetracker.entities.Users;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class JWTService {
    public static final String SECRET = "ZXJ0MzR0MzQ1NjU0MzQ1NjQzNDU0MzI0NTQzNDU0MzQ0NTQzNDU0NDU2NTQz";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        Date expiry = extractExpiration(token);
        return expiry.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userInfo){
        final String username = extractUsername(token);
        return username.equals(userInfo.getUsername()) && !isTokenExpired(token);
    }

    public String createToken(Map<String, Object> claims, String username) {
        int expiryTime = 1000 * 60 * 1;
        return Jwts
                .builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {

        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
