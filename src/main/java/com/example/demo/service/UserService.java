package com.example.demo.service;

import com.example.demo.dto.UserResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        return new UserResponse(findUserById(id));
    }

    public UserResponse updateUser(Long id, User updatedUser) {
        User existing = findUserById(id);

        if (updatedUser.getName() != null && !updatedUser.getName().isBlank()) {
            existing.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank()) {
            existing.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getRole() != null) {
            existing.setRole(updatedUser.getRole());
        }

        return new UserResponse(userRepository.save(existing));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
