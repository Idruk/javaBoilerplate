package com.example.springboot.controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.service.UserService;
import com.example.springboot.entity.User;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService UserService;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return UserService.findAllUser();
	}

	@PostMapping("/register")
	public ResponseEntity<Object> signin(@RequestParam Map<String, String> json_Input) {
		String username = json_Input.getOrDefault("username", null);
		String password = json_Input.getOrDefault("password", null);
		Map<String, Object> respError = new HashMap<String, Object>();

		List<User> users;
		Iterator<User> iterator;

		if (username == null || password == null) {
			respError.put("message", "Error in validation /user/register");
			return ResponseEntity.ok(respError);
		}

		users = UserService.findAllUser();
		iterator = users.iterator();

		while (iterator.hasNext()) {
			User elem = iterator.next();

			if (elem.getUsername().equals(username)) {
				respError.put("message", "Error username already exist");

				return ResponseEntity.ok(respError);
			}
		}

		User resp = UserService.saveUser(
				new User(username, password, null));

		return ResponseEntity.ok(resp);
	}
}
