package com.hugbo.clock_in.auth;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.hugbo.clock_in.domain.entity.User;

public class CustomUserDetails implements UserDetails {
    public final User user;
    public final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public User getUser() {
        return user;
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public Long getId() {
        return user.id;
    }
    public String getName() {
        return user.name;
    }
    public String getUsername() {
        return user.email;
    }
    public String getPassword() {
        return user.password;
    }
    public Boolean getAdmin() {
        return user.admin;
    }
}
