package com.exercise.vendingmachine.service.impl;

import com.exercise.vendingmachine.dto.UserDto;
import com.exercise.vendingmachine.dto.UserDetailsDto;
import com.exercise.vendingmachine.exception.AccessDeniedException;
import com.exercise.vendingmachine.exception.EntityNotFoundException;
import com.exercise.vendingmachine.model.User;
import com.exercise.vendingmachine.repository.UserRepository;
import com.exercise.vendingmachine.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    /*
     * Create user can be called by anyone
     */
    @Override
    @Transactional
    public User createUser(UserDto userDto) {

        User user = User.builder()
                .username(userDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .deposit(0L)
                .role(userDto.getRole())
                .build();
        user = this.userRepository.save(user);
        LOG.info("USER CREATED");
        return user;
    }

    @Override
    public User getUser(UserDetailsDto userDetailsDto, Long userId) {
        checkUserPermission(userDetailsDto, userId);
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        LOG.info("User Fetch Successfully.");
        return user;
    }

    @Override
    @Transactional
    public User updateUser(UserDetailsDto userDetailsDto, Long userId, UserDto userDto) {
        checkUserPermission(userDetailsDto, userId);
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        user.setUsername(userDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user = this.userRepository.saveAndFlush(user);
        LOG.info("User Updated");
        return user;
    }

    @Override
    @Transactional
    public User deleteUser(UserDetailsDto userDetailsDto, Long userId) {
        checkUserPermission(userDetailsDto, userId);
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        this.userRepository.delete(user);
        LOG.info("User Deleted");
        return user;
    }

    /*
     * Strict security policy applied, users can only get or modify their own accounts
     */
    private static void checkUserPermission(UserDetailsDto userDetailsDto, Long userId) {
        if (!userDetailsDto.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }
    }

}