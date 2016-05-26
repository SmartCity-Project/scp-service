package com.smartcity.rest.controller.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="/api/test")
public class TestRestController
{
	@RequestMapping(value="/")
	public String root() {
		
		return "This is test";
	}

}
