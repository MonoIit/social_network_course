package com.example.auth_service.datamodel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<UserDAO, Long> {
    Optional<UserDAO> findFirstByLogin(String login);

    Optional<UserDAO> findFirstByEmail(String email);
}
