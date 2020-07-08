package com.personalsoft.sqlproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

	@Autowired
	UserController userControler;

	@Autowired
	MockMvc mock;

	@MockBean
	UserDao dao;

//	@Test
//	void contextLoads() {controller.createUser(null);}

	@Test
	void user_UT01_CreateUserSuccess_ReturnOkAndAnUser() throws Exception {
		int Edad = 15;
		String Nombre = "Edier Andres";
		String Correo = "edier@test.com";

		logger.info("user_UT01_CreateUserSuccess_ReturnOkAndAnUser");
		// GIVEN
		UserDto userDto = UserDto.builder().name(Nombre).email(Correo).age(Edad).build();
		// UserEntity userEntityReq = UserEntity.builder().name(Nombre).email(Correo).age(Edad).build();
		UserEntity userEntityRes = UserEntity.builder().id(1).name(Nombre).email(Correo).age(Edad).build();

		when(dao.save(any(UserEntity.class))).thenReturn(userEntityRes);

		// WHEN
//		UserEntity userEntity = userControler.save(userDto);
		MvcResult mvcRes = getResult(userDto);
		String userEntityJson = mvcRes.getResponse().getContentAsString();
		UserEntity userEntity = mapper.readValue(userEntityJson, UserEntity.class);
		// THEN
		assertEquals(userEntity.getName(), "Edier Andres");
		assertEquals(userEntity.getEmail(), "edier@test.com");
		assertNotNull(userEntity.getId());
		assertTrue(userEntity.getAge() >= 18);
	}

	private MvcResult getResult(UserDto requestObject) throws Exception {
		String json = mapper.writeValueAsString(requestObject);

		return this.mock.perform(
				post("/").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json))
				.andReturn();
	}

}
