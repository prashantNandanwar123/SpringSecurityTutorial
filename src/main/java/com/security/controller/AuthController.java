package com.security.controller;

import java.util.Collections;

import com.security.config.JwtTokenProvider;
import com.security.entity.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.security.entity.User;
import com.security.payload.JWTAuthResponse;
import com.security.payload.LoginDto;
import com.security.payload.Response;
import com.security.payload.SignUpDto;
import com.security.repository.RoleRepository;
import com.security.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger logger = LogManager.getLogger(AuthController.class);
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {
	    logger.info("AuthController || authenticateUser || called ");

	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    String token = tokenProvider.generateToken(authentication);

	    logger.info("JWT generated successfully");
	    return ResponseEntity.ok(new JWTAuthResponse(token));
	}

	@PostMapping("/signup")
	public Response registerUser(@RequestBody SignUpDto signUpDto) {
		logger.info("AuthController || registerUser || called");
		String name = signUpDto.getName();
		System.out.println("name::" + name);
		Response response = new Response();
		// add check for username exists in a DB
		String username = userRepository.getUsername(signUpDto.getUsername());
		System.out.println("----------Username-----" + username);
		if (username != (signUpDto.getUsername())) {
			System.out.println("----------IfUsername-----");
			response.setRespCode(1);
			response.setRespMsg("Username is already taken!");

		}

		// add check for email exists in DB
		String email = userRepository.getEmail(signUpDto.getEmail());
		System.out.println("----------Email-----" + email);
		if (email != (signUpDto.getEmail())) {
			System.out.println("----------ifEmail-----");
			response.setRespCode(1);
			response.setRespMsg("Email is already taken!");
		}

		// create user object
		User user = new User();
		user.setName(signUpDto.getName());
		user.setUsername(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());
		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

//		Role roles = roleRepository.findByName("ROLE_ADMIN").get();
//		System.out.println("---roles---" + roles);
//		user.setRoles(Collections.singleton(roles));
		

		userRepository.save(user);
		response.setRespCode(0);
		response.setRespMsg("User registered successfully");
		return response;

	}
}
