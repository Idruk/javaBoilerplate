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
import com.example.springboot.utils.JwtUtils;
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

	@GetMapping("/tokenTest")
	public ResponseEntity<Object> tokenTest() {
		return ResponseEntity.ok("oui");
	}

	@PostMapping("/signin")
	public ResponseEntity<Object> signin(@RequestParam Map<String, String> json_Input) {
		String username = json_Input.getOrDefault("username", null);
		String password = json_Input.getOrDefault("password", null);
		Map<String, Object> resp = new HashMap<String, Object>();

		if (username == null || password == null) {
			resp.put("message", "Error in validation /user/signin");
			return ResponseEntity.ok(resp);
		}

		Optional<User> check = UserService.findUsername(username);

		if (check.isPresent() == false) {
			resp.put("message", "Error User does not exist");
			return ResponseEntity.ok(resp);
		}

		User user = check.get();

		boolean passwordMatches = encoder.matches(password, user.getpassword());

		if (!passwordMatches) {
			resp.put("message", "Error invalid username or password");
			return ResponseEntity.ok(resp);
		}

		System.out.println(user.getId());

		System.out.println(JwtUtils.generateJwt(String.valueOf(user.getId())));

		resp.put("token", JwtUtils.generateJwt(String.valueOf(user.getId())));
		return ResponseEntity.ok(resp);
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
