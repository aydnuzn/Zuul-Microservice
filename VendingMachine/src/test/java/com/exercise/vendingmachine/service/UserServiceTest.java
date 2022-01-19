package com.exercise.vendingmachine.service;

import com.exercise.vendingmachine.dto.UserDetailsDto;
import com.exercise.vendingmachine.dto.UserDto;
import com.exercise.vendingmachine.enumeration.UserRole;
import com.exercise.vendingmachine.model.User;
import com.exercise.vendingmachine.repository.UserRepository;
import com.exercise.vendingmachine.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    User RECORD_1 = new User(1L,"aydin", "123456", 1L, UserRole.BUYER);
    User RECORD_2 = new User(2L,"demo", "1234567", 2L,UserRole.BUYER);

    @Test
    public void getUserById_success() throws Exception {
        UserDetailsDto userDetailsDto = new UserDetailsDto(RECORD_1);

        Mockito.when(userRepository.findById(RECORD_1.getId())).thenReturn(Optional.of(RECORD_1));

        assertEquals(userServiceImpl.getUser(userDetailsDto, 1L),RECORD_1);
    }

    @Test
    public void createUserById_success() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("aydin");
        userDto.setPassword(bCryptPasswordEncoder.encode("12332112"));
        userDto.setRole(UserRole.BUYER);

        User record = User.builder()
                .id(null)
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .deposit(0L)
                .role(UserRole.BUYER)
                .build();

        Mockito.when(userRepository.save(record)).thenReturn(record);

        assertEquals(userServiceImpl.createUser(userDto).getUsername(), record.getUsername());
    }

    @Test
    public void deleteUserById_success() throws Exception {
        UserDetailsDto userDetailsDto = new UserDetailsDto(RECORD_2);

        Mockito.when(userRepository.findById(RECORD_2.getId())).thenReturn(Optional.of(RECORD_2));

        assertEquals(userServiceImpl.deleteUser(userDetailsDto, 2L), RECORD_2);
    }

    @Test
    public void updateUsersRecord_success() throws Exception {
        UserDetailsDto userDetailsDto = new UserDetailsDto(RECORD_2);

        UserDto userDto = new UserDto();
        userDto.setUsername("mahmut");
        userDto.setPassword("12332112");
        userDto.setRole(UserRole.BUYER);

        User updatedRecord = User.builder()
                .id(2L)
                .username(userDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .deposit(2L)
                .role(userDto.getRole())
                .build();

        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(RECORD_2));
        Mockito.when(userRepository.saveAndFlush(updatedRecord)).thenReturn(updatedRecord);

        assertEquals(userServiceImpl.updateUser(userDetailsDto, 2L, userDto), updatedRecord);
    }

}
