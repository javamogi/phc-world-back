package com.phcworld.mock;

import com.phcworld.common.exception.model.DeletedEntityException;
import com.phcworld.common.exception.model.NotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@RequiredArgsConstructor
@Builder
public class FakeUserDetailsService implements UserDetailsService {

    private final FakeUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(NotFoundException::new);
    }

    private UserDetails createUserDetails(com.phcworld.user.domain.User user) {
        if(user.isDeleted()){
            throw new DeletedEntityException();
        }
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAuthority().toString());
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()),
                user.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
