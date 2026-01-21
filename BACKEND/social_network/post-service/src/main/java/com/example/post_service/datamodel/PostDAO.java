package com.example.post_service.datamodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDAO {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private String text;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;
}
