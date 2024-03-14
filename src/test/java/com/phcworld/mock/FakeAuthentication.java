package com.phcworld.mock;

import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class FakeAuthentication {

    private final long id;

    public Authentication getAuthentication() throws UsernameNotFoundException {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(Authority.ROLE_USER.toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        UserDetails principal =  new User(
                String.valueOf(id),
                "test",
                authorities
        );

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
