package org.financetracker.services;

import org.financetracker.entities.RefreshToken;
import org.financetracker.entities.Users;
import org.financetracker.repository.RefreshTokenRepository;
import org.financetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired RefreshTokenRepository refreshTokenRepository;

    @Autowired UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        Users extractedUserInfo = userRepository.findByUsername(username);
        RefreshToken refreshToken = RefreshToken
                .builder()
                .userInfo(extractedUserInfo)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh Token has expired, Please login again");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
