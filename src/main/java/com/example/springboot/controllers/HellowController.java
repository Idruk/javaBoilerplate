package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HellowController {

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
}
