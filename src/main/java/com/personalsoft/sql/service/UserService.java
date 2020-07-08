package com.personalsoft.sql.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalsoft.sql.model.db.UserEntity;
import com.personalsoft.sql.model.dto.UserDto;
import com.personalsoft.sql.repository.UserDao;

@Service
public class UserService {

	@Autowired
	UserDao userDao;

	public List<UserEntity> list() {
		return (List<UserEntity>) userDao.findAll();
	}

	public UserEntity create(UserDto user) {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(user.getEmail());
		userEntity.setName(user.getName());
		return userDao.save(userEntity);
	}

	public UserEntity update(UserDto user, Integer id) {
		// get the user, if it doesn't exist put null
		UserEntity userEntity = userDao.findById(id).orElse(null);
		if (userEntity != null) {
			userEntity.setEmail(user.getEmail());
			userEntity.setName(user.getName());
			userDao.save(userEntity);
		}
		return userEntity;
	}

	public UserEntity delete(Integer id) {

		UserEntity userEntity = userDao.findById(id).orElse(null);
		if (userEntity != null) {
			userDao.delete(userEntity);
		}
		return userEntity;
	}
}
