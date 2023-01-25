package com.pos.idm.service.impl;

import com.pos.idm.entity.UserEntity;
import com.pos.idm.exception.BadCredentialsException;
import com.pos.idm.exception.UserNotFoundException;
import com.pos.idm.repository.UserRepository;
import com.pos.idm.service.UserService;
import com.pos.idm.soap.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserEntity findUserByCredentials(LoginRequest request) throws BadCredentialsException {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(BadCredentialsException::new);

        if(passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return user;
        else
            throw new BadCredentialsException();
    }

    @Override
    public void addUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserEntity findUserById(Integer id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user);
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
