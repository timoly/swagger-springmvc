package com.mangofactory.swagger.springmvc;

import java.util.List;

import lombok.Data;

@Data
public class AllowableValues {

	AllowableValueType valueType;
	List<String> values;
}
