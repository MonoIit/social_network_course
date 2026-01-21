package com.example.auth_service.service;

import com.example.auth_service.datamodel.UserDAO;
import com.example.auth_service.datamodel.UsersRepository;
import com.example.auth_service.model.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    public MyUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDAO userDAO = usersRepository.findFirstByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new MyUserDetails(userDAO.getLogin(), userDAO.getPasswordHash(), userDAO.getId());
    }
}
