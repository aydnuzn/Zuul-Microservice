package com.exercise.vendingmachine.service;

import com.exercise.vendingmachine.dto.UserDetailsDto;
import com.exercise.vendingmachine.enumeration.UserRole;
import com.exercise.vendingmachine.model.User;
import com.exercise.vendingmachine.repository.UserRepository;
import com.exercise.vendingmachine.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    User RECORD_1 = new User(1L,"aydin", "123456", 1L, UserRole.BUYER);

    @Test
    public void loadUserByUsername_success() throws Exception{
        Mockito.when(userRepository.findTopByUsername(RECORD_1.getUsername()))
                .thenReturn(Optional.of(RECORD_1));

        assertEquals(userDetailsServiceImpl.loadUserByUsername(RECORD_1.getUsername()), new UserDetailsDto(RECORD_1));
    }
}
