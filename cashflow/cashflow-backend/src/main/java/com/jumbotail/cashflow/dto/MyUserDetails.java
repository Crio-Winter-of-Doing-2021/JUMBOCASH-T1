package com.jumbotail.cashflow.dto;

import com.jumbotail.cashflow.models.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    private String userName;
    private String password;
    private String email;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(UserEntity userEntity) {
        this.userName = userEntity.getUserName();
        this.password = userEntity.getPassword();
        this.email = userEntity.getEmail();
        this.authorities = getAuthoritiesFromUserEntity(userEntity);
    }

    private List<GrantedAuthority> getAuthoritiesFromUserEntity(UserEntity userEntity) {
        if(userEntity.getRoles() == null || userEntity.getRoles().isEmpty()) {
            return new ArrayList<GrantedAuthority>();
        }
        else {
            return Arrays.stream(userEntity.getRoles().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }

    public MyUserDetails() {

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
