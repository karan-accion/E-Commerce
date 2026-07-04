package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {
    private String usersPath = "./data/users.json";

    public String getUsersPath() {
        return usersPath;
    }

    public void setUsersPath(String usersPath) {
        this.usersPath = usersPath;
    }
}
