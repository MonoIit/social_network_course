package com.example.auth_service.event;

import com.example.auth_service.datamodel.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDeleted {
    private Long id;
    private String login;
    private String email;

    public UserDeleted(UserDAO user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
    }
}
