package com.example.post_service.controller;

import com.example.post_service.dto.PostDTO;
import com.example.post_service.dto.PostResponse;
import com.example.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {
    private final PostService service;

    @GetMapping
    public List<PostResponse> getPosts() {
        return service.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostResponse getPostById(
            @PathVariable Long id
    ) {
        return service.getPost(id);
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPostPhoto(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(service.getPhoto(id));
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void createPost(
            @RequestPart("data") PostDTO data,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws IOException {
        service.createPost(data, photo);
    }
}
