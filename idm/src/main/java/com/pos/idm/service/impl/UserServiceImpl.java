package com.pos.idm.service.impl;

import com.pos.idm.entity.UserEntity;
import com.pos.idm.exception.BadCredentialsException;
import com.pos.idm.repository.UserRepository;
import com.pos.idm.service.UserService;
import com.pos.idm.soap.dto.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserEntity findUserByCredentials(AuthRequest request) throws BadCredentialsException {
        UserEntity user = repository.findByUsername(request.getUsername())
                                    .orElseThrow(BadCredentialsException::new);

        if(passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return user;
        else
            throw new BadCredentialsException();
    }

    @Override
    public boolean checkUsernameExists(String username) throws BadCredentialsException {
        return repository.findByUsername(username).isPresent();
    }

    @Override
    public void addUser(UserEntity user) {
        repository.save(user);
    }
}
