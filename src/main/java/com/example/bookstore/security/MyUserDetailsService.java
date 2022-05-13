package com.example.bookstore.security;

import com.example.bookstore.entity.PermissionEntity;
import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.repository.UserJpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userRepository;

    public MyUserDetailsService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findPermissionsByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with login '" + username +"' not found"));

        return User.builder()
                .username(userEntity.getLogin())
                .password(userEntity.getPassword())
                .authorities(
                        userEntity.getPermissions().stream()
                                .map(PermissionEntity::getName)
                                .map(Enum::name)
                                .map(SimpleGrantedAuthority::new)
                                .toList()
                ).build();
    }

}
