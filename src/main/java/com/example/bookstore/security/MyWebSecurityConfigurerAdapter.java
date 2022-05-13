package com.example.bookstore.security;

import com.example.bookstore.entity.Permission;
import com.example.bookstore.repository.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class MyWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final UserJpaRepository userRepository;

    public MyWebSecurityConfigurerAdapter(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/create-book", "/create-book/**").hasAuthority(Permission.CREATE_BOOK.name())
                    .antMatchers("/user", "/user/**").authenticated()
                    .antMatchers("/favourites", "/favourites/**").authenticated()
                    .anyRequest().permitAll()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new MyUserDetailsService(userRepository);
    }

}
