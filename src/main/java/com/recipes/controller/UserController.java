package com.recipes.controller;

import com.recipes.dto.UserDTO;
import com.recipes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * REST Controller for user management
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management and profiles")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<UserDTO> getUserProfile() {
        UserDTO user = userService.getUserProfile();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Update current user profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateProfile(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/{id}/follow")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Follow a user")
    public ResponseEntity<Void> followUser(@PathVariable Long id) {
        userService.followUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/follow")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Unfollow a user")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long id) {
        userService.unfollowUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/followers")
    @Operation(summary = "Get user followers")
    public ResponseEntity<Set<UserDTO>> getFollowers(@PathVariable Long id) {
        Set<UserDTO> followers = userService.getFollowers(id);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{id}/following")
    @Operation(summary = "Get users followed by user")
    public ResponseEntity<Set<UserDTO>> getFollowing(@PathVariable Long id) {
        Set<UserDTO> following = userService.getFollowing(id);
        return ResponseEntity.ok(following);
    }
}
