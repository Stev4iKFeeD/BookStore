package com.example.bookstore.repository;

import com.example.bookstore.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByLogin(String login);

    @Query("""
            SELECT u
            FROM UserEntity u
            WHERE u.login = :login
            """)
    Optional<UserEntity> findBasicByLogin(@Param("login") String login);

    @Query("""
            SELECT u
            FROM UserEntity u
                LEFT JOIN FETCH u.favourites
            WHERE u.login = :login
            """)
    Optional<UserEntity> findFavouritesByLogin(@Param("login") String login);

    @Query("""
            SELECT u
            FROM UserEntity u
                LEFT JOIN FETCH u.permissions
            WHERE u.login = :login
            """)
    Optional<UserEntity> findPermissionsByLogin(@Param("login") String login);

//    @Query("""
//            SELECT u
//            FROM UserEntity u
//                LEFT JOIN FETCH u.favourites
//                LEFT JOIN FETCH u.permissions
//            WHERE u.login = :login
//            """)
//    Optional<UserEntity> findFullByLogin(@Param("login") String login);

}
