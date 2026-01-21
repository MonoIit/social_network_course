package com.example.post_service.datamodel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<PostDAO, Long> {
    Optional<PostDAO> findById(Long id);
}
