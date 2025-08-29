package org.financetracker.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.financetracker.DTOs.request.UserInfoDto;
import org.financetracker.entities.Users;
import org.financetracker.repository.UserRepository;
import org.financetracker.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Users user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("could not find user....");
        }
        return new CustomUserDetails(user);
    }

    public Users checkIfUserAlreadyExists(UserInfoDto user){
        return userRepository.findByUsername(user.getUsername());
    }

    public Boolean signUpUser(UserInfoDto user) throws IllegalAccessError{
        Boolean isValidUser = Validation.validateUserEmailAndPassword(user);
        if(!isValidUser) {
            throw new IllegalAccessError("Invalid user Details");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(Objects.nonNull(checkIfUserAlreadyExists(user))){
            return false;
        }

        String userId = UUID.randomUUID().toString();
        userRepository.save(new Users(userId, user.getUsername(), user.getPassword(), new HashSet<>()));
        return true;
    }
 }
