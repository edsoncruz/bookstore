package com.bookstore.entity;

import com.bookstore.entity.base.BaseEntity;
import com.bookstore.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        switch (role){
            case ADMIN ->  {
                grantedAuthorities.add( new SimpleGrantedAuthority("ROLE_ADMIN"));
                grantedAuthorities.add( new SimpleGrantedAuthority(Role.CUSTOMER.name()));
            }
            case CUSTOMER -> {
                grantedAuthorities.add( new SimpleGrantedAuthority(Role.CUSTOMER.name()));
            }
        }

        return grantedAuthorities;
    }
}
