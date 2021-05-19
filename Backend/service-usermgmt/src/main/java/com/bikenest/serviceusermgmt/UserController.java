package com.bikenest.serviceusermgmt;

import com.bikenest.serviceusermgmt.helper.JWTHelper;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;

import java.security.Key;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikenest.serviceusermgmt.models.User;
import com.bikenest.serviceusermgmt.payload.LoginRequest;
import com.bikenest.serviceusermgmt.payload.SignupRequest;
import com.bikenest.serviceusermgmt.payload.MessageResponse;
import com.bikenest.serviceusermgmt.repository.UserRepository;

@RestController
@RequestMapping(path="/usermanagement")
public class UserController {
    private Key SECRET_KEY = Keys.hmacShaKeyFor("NdRgUkXp2s5v8yzB?D(G+KbPeShVmYq3".getBytes());

	@Autowired
	UserRepository userRepository;

    //jwtauth Endpoint
    @PostMapping(path="/validatejwt")
    public ResponseEntity<Boolean> ValidateJWT(@RequestBody String JWT){
    	return ResponseEntity.ok(JWTHelper.GetSingleton().ValidateJWT(JWT));
    }

	@PostMapping("/signin")
	public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
		if(!user.isPresent()){
			return ResponseEntity.badRequest().body("Username not found!");
		}
		if(loginRequest.getPassword().equals(user.get().getPassword()))
		{
			String jwt = JWTHelper.GetSingleton().BuildJwtFromUser(user.get());
			return ResponseEntity.ok(jwt);
		}
		return ResponseEntity.badRequest().body("Invalid password provided!");
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 signUpRequest.getPassword());

		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}

