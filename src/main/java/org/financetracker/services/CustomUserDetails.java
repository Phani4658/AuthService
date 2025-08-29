package org.financetracker.services;

import org.financetracker.entities.Roles;
import org.financetracker.entities.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends Users implements UserDetails {
    private String username;

    private String password;

    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Users userInfo){
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();

        for(Roles role: userInfo.getRoles()){
            auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }
        this.authorities = auths;
    }


    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
