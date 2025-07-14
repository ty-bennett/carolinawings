package com.carolinawings.webapp.security.service;

import com.carolinawings.webapp.dto.RestaurantDTO;
import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private static ModelMapper modelMapper;

    private UUID id;
    private String username;
    private String name;
    private Set<Long> restaurants;
    @JsonIgnore
    private String password;
    private Collection<? extends  GrantedAuthority> authorities;

    public UserDetailsImpl(UUID id, String username,String name, String password, Collection<? extends GrantedAuthority> authorities, Set<Long> restaurants) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.authorities = authorities;
        this.restaurants = restaurants;
    }

    public static UserDetailsImpl build(User user)
    {
        List<SimpleGrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .toList();
        Set<Long> restaurants = user.getRestaurants().stream()
                .map(Restaurant::getId)
                .collect(Collectors.toSet());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                grantedAuthorities,
                restaurants
        );
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
        return username;
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
        return  true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return this.name;
    }

    public Set<Long> getRestaurants()
    {
        return this.restaurants;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
