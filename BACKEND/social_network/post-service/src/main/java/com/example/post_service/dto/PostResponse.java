package com.example.post_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private Long userId;
    private String Text;
    private Boolean hasPhoto;
}
