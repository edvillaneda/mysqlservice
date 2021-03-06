package com.personalsoft.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalsoft.sql.controller.UserController;
import com.personalsoft.sql.model.db.UserEntity;
import com.personalsoft.sql.model.dto.UserDto;
import com.personalsoft.sql.repository.UserDao;
import com.personalsoft.sql.service.UserService;

/**
 * @author Edier Andrés
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MySqlProjectApplication.class)
@WebMvcTest({ UserController.class, UserService.class })
class MySqlProjectApplicationTests {
	private static final Logger logger = LoggerFactory.getLogger(MySqlProjectApplicationTests.class);

	private ObjectMapper mapper = new ObjectMapper();

	int Edad = 25;
	String Nombre = "Edier Andres";
	String Correo = "edier@test.com";

	@Autowired
	UserController userControler;

	@Autowired
	MockMvc mock;

	@MockBean
	UserDao dao;

	UserDto userDto;
	UserEntity userEntityRes;

	@BeforeEach
	void contextLoads() {
		userDto = UserDto.builder().name(Nombre).email(Correo).age(Edad).build();
		userEntityRes = UserEntity.builder().id(1).name(Nombre).email(Correo).age(Edad).build();
	}

	@Test
	void user_UT01_CreateUserSuccess_ReturnOkAndAnUser() throws Exception {

		logger.info("user_UT01_CreateUserSuccess_ReturnOkAndAnUser");

		// GIVEN
		when(dao.save(any(UserEntity.class))).thenReturn(userEntityRes);

		// WHEN
		MvcResult mvcRes = getResultPost(userDto);
		String userEntityJson = mvcRes.getResponse().getContentAsString();
		UserEntity userEntity = mapper.readValue(userEntityJson, UserEntity.class);

		// THEN
		assertEquals(userDto.getName(), userEntity.getName());
		assertEquals(userDto.getEmail(), userEntity.getEmail());
		assertNotNull(userEntity.getId());
		assertTrue(userEntity.getAge() >= 18);
	}

	@Test
	void user_UT02_UpdateUserSuccess_ReturnOkAndAnUser() throws Exception {

		logger.info("user_UT02_CreateUserSuccess_ReturnOkAndAnUser");
		
		String nameUp = "Andres"; 
		String emailup = "Andres@test.com";

		// GIVEN
		userDto.setId(1);
		userDto.setAge(20);
		
		userEntityRes = UserEntity.builder().id(1).name(Nombre).email(Correo).age(24).build();
		Optional<UserEntity> userResOpt = Optional.of(userEntityRes);

		UserEntity userEntityResEdited = UserEntity.builder().id(1).name(nameUp).email(emailup).build();

		when(dao.findById(any(Integer.class))).thenReturn(userResOpt);
		when(dao.save(any(UserEntity.class))).thenReturn(userEntityResEdited);

		// WHEN
//		UserEntity userEntity = userControler.updateUser(userDto, 1);

		MvcResult mvcRes = getResultPut(userDto, 1);
		String userEntityJson = mvcRes.getResponse().getContentAsString();
		UserEntity userEntity = mapper.readValue(userEntityJson, UserEntity.class);

		// THEN
		assertNotEquals(nameUp, userEntity.getName());
		assertNotEquals(emailup, userEntity.getEmail());
		assertTrue(userEntity.getAge() >= 18);
	}

	@Test
	void user_UT03_UpdateUserSuccess_ReturnOkAndAnUser() throws Exception {

		logger.info("user_UT03_CreateUserSuccess_ReturnOkAndAnUser");

		String nameUp = "Villaneda";
		String emailUp = "Andres@test.com";
		Integer UpdateAge = 20;

		userDto.setId(1);
		userDto.setAge(25);

		// GIVEN
		userEntityRes = UserEntity.builder().id(1).name(Nombre).email(Correo).age(25).build();
		Optional<UserEntity> userResOpt = Optional.of(userEntityRes);

		UserEntity userEntityResEdited = UserEntity.builder().id(1).name(nameUp).age(UpdateAge).build();

		when(dao.findById(any(Integer.class))).thenReturn(userResOpt);
		when(dao.save(any(UserEntity.class))).thenReturn(userEntityResEdited);

		// WHEN
//		UserEntity userEntity = userControler.updateUser(userDto, 1);

		MvcResult mvcRes = getResultPut(userDto, 1);
		String userEntityJson = mvcRes.getResponse().getContentAsString();
		UserEntity userEntity = mapper.readValue(userEntityJson, UserEntity.class);

		// THEN
		assertNotEquals(nameUp, userEntity.getName());
		assertNotEquals(emailUp, userEntity.getEmail());
		assertTrue(userEntity.getAge() >= 18);
		assertNotEquals(UpdateAge, userEntity.getAge());
	}

//	@Test
	void user_UT04_UpdateUserNotSuccess_ReturnNotOk() throws Exception {

		logger.info("user_UT04_UpdateUserNotSuccess_ReturnNotOk");

		String nameUp = "Villaneda";
		Integer UpdateAge = 20;

		userDto.setId(1);

		// GIVEN
		userEntityRes = UserEntity.builder().id(10).name(Nombre).email(Correo).age(25).build();
		Optional<UserEntity> userResOpt = Optional.of(userEntityRes);

		UserEntity userEntityResEdited = UserEntity.builder().id(0).name(nameUp).age(UpdateAge).build();

		when(dao.findById(any(Integer.class))).thenReturn(userResOpt);
		when(dao.save(any(UserEntity.class))).thenReturn(userEntityResEdited);

		// WHEN
//		UserEntity userEntity = userControler.updateUser(userDto, 1);

		MvcResult mvcRes = getResultPut(userDto, 10);
		String userEntityJson = mvcRes.getResponse().getContentAsString();
		UserEntity userEntity = mapper.readValue(userEntityJson, UserEntity.class);

		// THEN
		assertEquals(null, userEntity);
	}

	private MvcResult getResultPost(UserDto requestObject) throws Exception {
		String json = mapper.writeValueAsString(requestObject);

		return this.mock.perform(
				post("/").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json))
				.andReturn();
	}

	private MvcResult getResultPut(UserDto requestObject, Integer id) throws Exception {
		String json = mapper.writeValueAsString(requestObject);

		return this.mock.perform(put("/".concat(String.valueOf(id))).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();
	}

}
