package com.personalsoft.sqlproject.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.personalsoft.sqlproject.model.db.UserEntity;
import com.personalsoft.sqlproject.model.dto.UserDto;
import com.personalsoft.sqlproject.service.UserService;

@RestController
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	
	@GetMapping
	public ResponseEntity<List<UserEntity>> getAll(){
		return ResponseEntity.ok(userService.list());
	}
	
	@PostMapping
	public UserEntity createUser(@RequestBody UserDto user) {
		logger.info("Creating new user");
		logger.warn("Creating new user warning");
		logger.error("Creating new user error");
		logger.info("createUser(): Email: {}, Name: {}", user.getEmail(), user.getName());
		logger.info("createUser(): {}", user);
		return userService.create(user);
	}
	
	@PutMapping ("/{id}")
	public UserEntity updateUser(
			@RequestBody UserDto user, 
			@PathVariable Integer id) {
		return userService.update(user, id);
	}
	
}
