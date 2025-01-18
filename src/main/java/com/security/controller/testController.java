package com.security.controller;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.entity.User;
import com.security.payload.Response;

@RestController
@RequestMapping("/api")
public class testController {
	@PostMapping("test")
	public Response test(@RequestBody User user) {
		Response response = new Response();
		System.out.println("test");
		response.setRespCode(1);
		response.setRespMsg("Failed");
		return response;
	}
}
