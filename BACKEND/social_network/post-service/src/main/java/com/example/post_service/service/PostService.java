package com.example.post_service.service;

import com.example.post_service.datamodel.PostDAO;
import com.example.post_service.datamodel.PostRepository;
import com.example.post_service.dto.PostDTO;
import com.example.post_service.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final ModelMapper mapper;

    public PostResponse getPost(Long id) {
        PostDAO post = repository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        return new PostResponse(
                post.getId(),
                post.getUserId(),
                post.getText(),
                post.getPhoto() != null
        );
    }

    public List<PostResponse> getAllPosts() {
        List<PostDAO> posts = (List<PostDAO>) repository.findAll();

        return posts.stream()
                .map(e -> new PostResponse(
                        e.getId(),
                        e.getUserId(),
                        e.getText(),
                        e.getPhoto() != null
                ))
                .toList();
    }

    public void createPost(PostDTO post, MultipartFile photo) throws IOException {
        PostDAO postDAO = new PostDAO();
        postDAO.setUserId(post.getUserId());
        postDAO.setText(post.getText());
        postDAO.setPhoto(photo != null ? photo.getBytes() : null);

        repository.save(postDAO);
    }

    public byte[] getPhoto(Long id) {
        PostDAO post = repository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        return post.getPhoto();
    }
}
