package com.example.user_service.datamodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDAO {
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(unique = true, nullable = false)
    private String email;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;

    public UserDAO(Long id, String login, String email) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.photo = null;
    }
}
