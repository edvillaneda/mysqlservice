package com.personalsoft.sql.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.personalsoft.sql.model.db.UserEntity;
import com.personalsoft.sql.model.dto.UserDto;
import com.personalsoft.sql.service.UserService;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@GetMapping
	public ResponseEntity<List<UserEntity>> getAll() {
		return ResponseEntity.ok(userService.list());
	}

	@PostMapping
	@ResponseBody
	public UserEntity createUser(@Valid @RequestBody UserDto user) {
		return userService.create(user);
	}

	@PutMapping("/{id}")
	public UserEntity updateUser(@RequestBody UserDto user, @PathVariable Integer id) {
		logger.info("updateUser(): Id: {}, Email: {}, Name: {}", id, user.getEmail(), user.getName());
		return userService.update(user, id);
	}

	@DeleteMapping("/{id}")
	public UserEntity deleteUser(@PathVariable Integer id) {
		logger.info("updateUser(): Id: {}", id);
		return userService.delete(id);
	}

//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

}
