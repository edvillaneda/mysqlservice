package com.personalsoft.sql.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.personalsoft.sql.model.db.UserEntity;

@Repository
public interface UserDao extends CrudRepository<UserEntity, Integer> {

}