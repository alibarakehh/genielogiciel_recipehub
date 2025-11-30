package com.recipes.service;

import com.recipes.dto.UserDTO;
import com.recipes.exception.ResourceNotFoundException;
import com.recipes.exception.UnauthorizedException;
import com.recipes.model.User;
import com.recipes.repository.RecipeRepository;
import com.recipes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for user management operations
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return convertToDTO(user);
    }

    public UserDTO getUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO updateProfile(UserDTO userDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Update fields
        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getBio() != null) {
            user.setBio(userDTO.getBio());
        }
        if (userDTO.getProfileImageUrl() != null) {
            user.setProfileImageUrl(userDTO.getProfileImageUrl());
        }

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public void followUser(Long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        User userToFollow = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (currentUser.getId().equals(userId)) {
            throw new UnauthorizedException("You cannot follow yourself");
        }

        currentUser.getFollowing().add(userToFollow);
        userRepository.save(currentUser);
    }

    @Transactional
    public void unfollowUser(Long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        User userToUnfollow = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        currentUser.getFollowing().remove(userToUnfollow);
        userRepository.save(currentUser);
    }

    public Set<UserDTO> getFollowers(Long userId) {
        Set<User> followers = userRepository.findFollowers(userId);
        return followers.stream().map(this::convertToDTO).collect(Collectors.toSet());
    }

    public Set<UserDTO> getFollowing(Long userId) {
        Set<User> following = userRepository.findFollowing(userId);
        return following.stream().map(this::convertToDTO).collect(Collectors.toSet());
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setBio(user.getBio());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setRole(user.getRole().name());
        dto.setRecipeCount(recipeRepository.countByAuthorId(user.getId()).intValue());
        dto.setFollowerCount(user.getFollowers().size());
        dto.setFollowingCount(user.getFollowing().size());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
