package com.personalsoft.sql.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

	private Integer id;
	@Size(min = 10)
	private String name;
	private String email;
	@Min(18)
	private Integer age;

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", name=" + name + ", email=" + email + "]";
	}
}
