package com.example.auth_service.service;

import com.example.auth_service.datamodel.UserDAO;
import com.example.auth_service.datamodel.UsersRepository;
import com.example.auth_service.dto.*;
import com.example.auth_service.event.UserCreated;
import com.example.auth_service.event.UserDeleted;
import com.example.auth_service.model.BadInputParameters;
import com.example.auth_service.model.ConflictDataException;
import com.example.auth_service.model.MyUserDetails;
import com.example.auth_service.model.UserNotFoundException;
import com.example.auth_service.properties.KafkaProperties;
import com.example.auth_service.security.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UsersRepository repository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailsService myUserDetailsService;
    private final KafkaTemplate<String, UserCreated> userCreatedKafkaTemplate;
    private final KafkaTemplate<String, UserDeleted> userDeletedKafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        final String login = request.getLogin();
        final String email = request.getEmail();

        validateEmail(email);
        validateLogin(login);
        validatePassword(request.getPassword());

        final String passwordHash = passwordEncoder.encode(request.getPassword());

        try {
            UserDAO saved  = repository.save(new UserDAO(login, email, passwordHash));
            userCreatedKafkaTemplate.send(kafkaProperties.getTopics().get("userCreated"), new UserCreated(saved));
            return new RegisterResponse(saved.getId());
        } catch (DataIntegrityViolationException e) {
            throw new ConflictDataException("User with given email is already exists!\n" + e.getMessage());
        }
    }

    @Override
    public AuthOkResponse login(LoginRequest request) {
        UserDAO userDAO = repository.findFirstByLogin(request.getLogin())
                .orElseThrow(() -> new BadInputParameters("Invalid password"));

        if (!passwordEncoder.matches(request.getPassword(), userDAO.getPasswordHash())) {
            throw new BadInputParameters("Invalid password");
        }
        String token = jwtTokenUtil.generateToken(new MyUserDetails(userDAO.getLogin(), userDAO.getPasswordHash(), userDAO.getId()));
        return new AuthOkResponse().authToken(token);
    }

    @Override
    public void logout(Long id) {
        UserDAO user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found!"));
    }

    @Override
    public void delete(Long id) {
        UserDAO user = repository.findById(id).orElse(null);
        repository.deleteById(id);
        if (user != null) {
            userDeletedKafkaTemplate.send(kafkaProperties.getTopics().get("userDeleted"), new UserDeleted(user));
        }
    }

    @Override
    public ValidateResponse validate(String token) {
        System.out.println("validate");

        String username = null;
        String userId = null;

        if (token != null) {
            try {
                username = jwtTokenUtil.getUsernameFromToken(token);
                userId = jwtTokenUtil.getUserIdFromToken(token);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        }

        if (username == null) {
            throw new IllegalArgumentException("Invalid token payload");
        }

        MyUserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

        if (!jwtTokenUtil.validateToken(token, userDetails)) {
            throw new IllegalArgumentException("Token validation failed");
        }

        return new ValidateResponse(userId);
    }

    private void validateEmail(String email) {
        if (!Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
                .matcher(email)
                .matches()) {
            throw new BadInputParameters("invalid email");
        }

        Optional<UserDAO> user = repository.findFirstByEmail(email);
        if (user.isPresent()) {
            throw new ConflictDataException("Пользователь с таким email уже существует");
        }
    }

    private void validateLogin(String login) {
        Optional<UserDAO> user = repository.findFirstByLogin(login);
        if (user.isPresent()) {
            throw new ConflictDataException("Пользователь с таким логином уже существует");
        }

        if (login.isEmpty()) {
            throw new BadInputParameters("login required");
        }
    }

    private void validatePassword(String password) {
        if (password.isEmpty()) {
            throw new BadInputParameters("password required");
        }
    }
}
