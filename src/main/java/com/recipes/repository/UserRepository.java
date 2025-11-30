package com.recipes.repository;

import com.recipes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for User entity
 * 
 * Provides database operations for User management
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN'")
    Set<User> findAllAdmins();

    @Query("SELECT u.followers FROM User u WHERE u.id = :userId")
    Set<User> findFollowers(Long userId);

    @Query("SELECT u.following FROM User u WHERE u.id = :userId")
    Set<User> findFollowing(Long userId);
}
