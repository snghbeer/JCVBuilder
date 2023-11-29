package com.example.jcvbuilder.models.DTO;

import java.util.Optional;

public class UserDTO {
    private String username, password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
