package com.example.bookstore.service;

import com.example.bookstore.entity.BookEntity;
import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

@Service
public class UserService {

    private final UserJpaRepository userRepository;

    public UserService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean existsUserByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Transactional
    public UserEntity createUser(String login, String password, String name) {
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(login);
        userEntity.setPassword(password);
        userEntity.setName(name);
        userEntity.setFavourites(new HashSet<>());
        userEntity.setPermissions(new ArrayList<>());
        return userRepository.saveAndFlush(userEntity);
    }

    @Transactional
    public UserEntity getUserByLogin(String login) {
        return userRepository.findBasicByLogin(login).orElse(null);
    }

    @Transactional
    public BookEntity getFavouriteByLogin(String login, String bookIsbn) {
        UserEntity userEntity = userRepository.findFavouritesByLogin(login).orElse(null);
        if (userEntity == null) {
            return null;
        }

        return userEntity.getFavourites().stream()
                .filter(bookEntity -> Objects.equals(bookEntity.getIsbn(), bookIsbn))
                .findAny().orElse(null);
    }

    @Transactional
    public UserEntity getUserWithFavouritesByLogin(String login) {
        return userRepository.findFavouritesByLogin(login).orElse(null);
    }

    @Transactional
    public boolean addToFavouritesByLogin(String login, BookEntity bookEntity) {
        UserEntity userEntity = userRepository.findFavouritesByLogin(login).orElse(null);
        if (userEntity == null) {
            return false;
        }

        userEntity.getFavourites().add(bookEntity);
        userRepository.saveAndFlush(userEntity);

        return true;
    }

    @Transactional
    public boolean removeFromFavouritesByLogin(String login, BookEntity bookEntity) {
        UserEntity userEntity = userRepository.findFavouritesByLogin(login).orElse(null);
        if (userEntity == null) {
            return false;
        }

        if (userEntity.getFavourites().remove(bookEntity)) {
            userRepository.saveAndFlush(userEntity);
            return true;
        }

        return false;
    }

}
