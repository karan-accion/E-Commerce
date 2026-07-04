package com.example.demo.repository;

import com.example.demo.config.StorageProperties;
import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final Path storagePath;
    private final ObjectMapper objectMapper;

    public UserRepository(StorageProperties storageProperties) {
        this.storagePath = Path.of(storageProperties.getUsersPath());
        this.objectMapper = new ObjectMapper();
    }

    public List<User> findAll() {
        try {
            if (Files.notExists(storagePath)) {
                return new ArrayList<>();
            }
            String content = Files.readString(storagePath);
            if (content == null || content.isBlank()) {
                return new ArrayList<>();
            }
            return List.of(objectMapper.readValue(content, User[].class));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read users from storage", e);
        }
    }

    public Optional<User> findById(UUID id) {
        return findAll().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public User save(User user) {
        List<User> users = new ArrayList<>(findAll());
        users.removeIf(existing -> existing.getId().equals(user.getId()));
        users.add(user);
        writeAll(users);
        return user;
    }

    public void deleteById(UUID id) {
        List<User> users = new ArrayList<>(findAll());
        users.removeIf(user -> user.getId().equals(id));
        writeAll(users);
    }

    private void writeAll(List<User> users) {
        try {
            Files.createDirectories(storagePath.getParent());
            objectMapper.writeValue(storagePath.toFile(), users);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write users to storage", e);
        }
    }
}
