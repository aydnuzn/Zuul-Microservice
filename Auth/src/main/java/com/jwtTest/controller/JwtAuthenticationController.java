package com.jwtTest.controller;

import com.jwtTest.dto.UserDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.jwtTest.config.JwtTokenUtil;


@RestController
@RequestMapping(
		value = "/auth",
		produces = MediaType.APPLICATION_JSON_VALUE,
		consumes = MediaType.APPLICATION_JSON_VALUE
)
public class JwtAuthenticationController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDto userDto) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username",userDto.getUsername());
		jsonObject.put("password",userDto.getPassword());
		String fullUserInfo = jsonObject.toString();

	   final UserDetails userDetails = userDetailsService
				.loadUserByUsername(fullUserInfo);
        final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(token);
	}

}
