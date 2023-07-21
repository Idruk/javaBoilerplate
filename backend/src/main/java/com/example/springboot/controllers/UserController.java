package com.example.springboot.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.entity.UserRepository;
import com.example.springboot.entity.User;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping("/register")
	public ResponseEntity<User> signin(@RequestParam Map<String, String> json_Input) {
		System.out.println(json_Input);
		System.out.println(json_Input.get(("username")));

		User resp = userRepository.save(new User(json_Input.get("username"), json_Input.get("password"), null));

		return ResponseEntity.ok(resp);
	}
}
