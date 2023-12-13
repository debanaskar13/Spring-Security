package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthInput;
import com.example.demo.entities.EmailDetails;
import com.example.demo.entities.User;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public Map<String,String> generateToken(@RequestBody AuthInput input) {
		Map<String,String> response = new HashMap<>();

		Authentication authenticate = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));

		if (authenticate.isAuthenticated()) {
			response.put("access_token",this.jwtService.generateToken(input.getUsername()));
			return response;
		}
		throw new UsernameNotFoundException("User Not Found");

	}

	@PostMapping("/forgot_password")
	public String forgotPassword(HttpServletRequest request,@RequestBody Map<String,String> input){
		String email = input.get("email");
		String token = RandomString.make(30);
		if(email!=null){
			this.userService.updateResetPasswordToken(token, email);
			String resetPasswordLink = new String(request.getRequestURL()).replace(request.getServletPath(), "") + "/auth/reset_password?token="+token;
			this.userService.sentEmail(EmailDetails.builder().recipient(email).build(),resetPasswordLink);
			return "Reset Password Link has been sent to you email";
		}else{
			return "Invalid Email";
		}
	}

	@PostMapping("/reset_password")
	public String resetPassword(@RequestBody Map<String,String> input){
		String token = input.get("token");
		String password = input.get("password");

		System.out.println("Token : "+token+"\n"+"Password is :"+password);

		if(token != null && password != null){
			User user = this.userService.getUserByResetPasswordToken(token);
			if(user == null){
				return "Invalid Token";
			}
			this.userService.updatePassword(user, password);
			return "Password Updated Successfully";
		}
		return "Please enter Json Body in Correct Format";
	}

	@GetMapping("/reset_password")
	public void showResetPage(@PathParam("token") String token){
		System.out.println("Token is  -----------------  "+token);
	}
}
