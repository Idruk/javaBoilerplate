package com.example.springboot.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

	private final BCryptPasswordEncoder encoder;

	@Autowired
	public UserController() {
		this.encoder = new BCryptPasswordEncoder(16);
	}

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return UserService.findAllUser();
	}

	@PostMapping("/signin")
	public ResponseEntity<Object> signin(@RequestParam Map<String, String> json_Input) {
		String username = json_Input.getOrDefault("username", null);
		String password = json_Input.getOrDefault("password", null);
		Map<String, Object> respError = new HashMap<String, Object>();

		if (username == null || password == null) {
			respError.put("message", "Error in validation /user/signin");
			return ResponseEntity.ok(respError);
		}

		Optional<User> check = UserService.findUsername(username);

		if (check.isPresent() == false) {
			respError.put("message", "Error User does not exist");
			return ResponseEntity.ok(respError);
		}

		User user = check.get();

		System.out.println(password);
		System.out.println(user.getpassword());

		boolean passwordMatches = encoder.matches(password, user.getpassword());

		if (!passwordMatches) {
			respError.put("message", "Error invalid username or password");
			return ResponseEntity.ok(respError);
		}

		return ResponseEntity.ok("ok");
	}

	@PostMapping("/register")
	public ResponseEntity<Object> register(@RequestParam Map<String, String> json_Input) {
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

		String encodedPaswd = encoder.encode(password);
		User resp = UserService.saveUser(
				new User(username, encodedPaswd, null));

		return ResponseEntity.ok(resp);
	}
}
