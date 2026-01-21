package com.example.user_service.datamodel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<UserDAO, Long> {

    Optional<UserDAO> findById(Long id);

}
