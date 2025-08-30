package org.financetracker.controller;

import org.financetracker.DTOs.request.AuthRequestDto;
import org.financetracker.DTOs.request.RefreshTokenDto;
import org.financetracker.DTOs.request.UserInfoDto;
import org.financetracker.DTOs.response.JWTResponseDto;
import org.financetracker.entities.RefreshToken;
import org.financetracker.services.JWTService;
import org.financetracker.services.RefreshTokenService;
import org.financetracker.services.UserDetailsServiceImpl;
import org.financetracker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class AuthController {
    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(Constants.Routes.SIGN_UP)
    public ResponseEntity signUp(@RequestBody UserInfoDto userInfo) {
        try {
            Boolean isUserSignedUp = userDetailsService.signUpUser(userInfo);
            if (!isUserSignedUp) {
                return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfo.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfo.getUsername());
            return new ResponseEntity<>(
                    JWTResponseDto
                            .builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken.getToken())
                            .build(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(Constants.Routes.SIGN_IN)
    public ResponseEntity signIn(@RequestBody AuthRequestDto authRequest){
        try {
            // check if user exists
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if(authentication.isAuthenticated()){
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
                String jwtToken = jwtService.GenerateToken(authRequest.getUsername());
                return new ResponseEntity(
                        JWTResponseDto
                                .builder()
                                .accessToken(jwtToken)
                                .refreshToken(refreshToken.getToken())
                                .build()
                        , HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid User Credentials", HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex){
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(Constants.Routes.REFRESH_TOKEN)
    public JWTResponseDto refreshToken(@RequestBody RefreshTokenDto refreshTokenDto){
        return refreshTokenService.findByToken(refreshTokenDto.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String jwtToken = jwtService.GenerateToken(userInfo.getUsername());
                    return new ResponseEntity<>(
                            JWTResponseDto
                                    .builder()
                                    .accessToken(jwtToken)
                                    .refreshToken(refreshTokenDto.getRefreshToken())
                                    .build(), HttpStatus.OK);
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in database!")).getBody();
    }
}
