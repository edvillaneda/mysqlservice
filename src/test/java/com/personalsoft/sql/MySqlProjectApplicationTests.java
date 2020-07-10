package com.personalsoft.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.personalsoft.sql.MySqlProjectApplication;
import com.personalsoft.sql.controller.UserController;
import com.personalsoft.sql.model.db.UserEntity;
import com.personalsoft.sql.model.dto.UserDto;
import com.personalsoft.sql.repository.UserDao;
import com.personalsoft.sql.service.UserService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MySqlProjectApplication.class)
@WebMvcTest({ UserController.class, UserService.class })
class MySqlProjectApplicationTests {
	private static final Logger logger = LoggerFactory.getLogger(MySqlProjectApplicationTests.class);

	private ObjectMapper mapper = new ObjectMapper();

	int Edad = 18;
	String Nombre = "Edier Andres";
	String Correo = "edier@test.com";

	@Autowired
	UserController userControler;

	@Autowired
	MockMvc mock;

	@MockBean
	UserDao dao;

	UserDto userDto;

	@BeforeEach
	void contextLoads() {
		userDto = UserDto.builder().name(Nombre).email(Correo).age(Edad).build();
	}

	@Test
	void user_UT01_CreateUserSuccess_ReturnOkAndAnUser() throws Exception {

		logger.info("user_UT01_CreateUserSuccess_ReturnOkAndAnUser");
		// GIVEN
		UserEntity userEntityRes = UserEntity.builder().id(1).name(Nombre).email(Correo).build();

		when(dao.save(any(UserEntity.class))).thenReturn(userEntityRes);

		// WHEN
//		UserEntity userEntity = userControler.save(userDto);
		MvcResult mvcRes = getResultPost(userDto);
		String userEntityJson = mvcRes.getResponse().getContentAsString();
		UserEntity userEntity = mapper.readValue(userEntityJson, UserEntity.class);
		// THEN
		assertEquals("Edier Andres", userEntity.getName());
		assertEquals("edier@test.com", userEntity.getEmail());
		assertNotNull(userEntity.getId());
		assertTrue(userDto.getAge() >= 18);
	}

	@Test
	void user_UT02_UpdateUserSuccess_ReturnOkAndAnUser() throws Exception {

		logger.info("user_UT01_CreateUserSuccess_ReturnOkAndAnUser");
		// GIVEN
		UserEntity userEntityRes = UserEntity.builder().id(1).name(Nombre).email(Correo).build();
		Optional<UserEntity> userResOpt = Optional.of(userEntityRes);

		UserEntity userEntityResEdited = UserEntity.builder().id(1).name("Andres").email(Correo).build();

		when(dao.findById(any(Integer.class))).thenReturn(userResOpt);
		when(dao.save(any(UserEntity.class))).thenReturn(userEntityResEdited);

		// WHEN
//		UserEntity userEntity = userControler.updateUser(userDto, 1);

		MvcResult mvcRes = getResultPut(userDto, 1);
		String userEntityJson = mvcRes.getResponse().getContentAsString();
		UserEntity userEntity = mapper.readValue(userEntityJson, UserEntity.class);

		// THEN
		assertEquals("Edier Andres", userEntity.getName());
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
