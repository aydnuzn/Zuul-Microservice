package com.jwtTest.service;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtTest.dto.UserDto;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto userDto = new ObjectMapper().readValue(username, UserDto.class);
		return new User(userDto.getUsername(), userDto.getPassword(), new ArrayList<>());
	}

}