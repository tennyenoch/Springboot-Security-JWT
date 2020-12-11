package com.springsecurity.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springsecurity.demo.configurer.EmpUserDetailsService;
import com.springsecurity.demo.model.AuthenticationRequest;
import com.springsecurity.demo.model.AuthenticationResponse;
import com.springsecurity.demo.util.JwtUtil;

@RestController
public class HelloWorldController {

@Autowired	
private AuthenticationManager authManager;	

@Autowired
EmpUserDetailsService empUserDetails;

@Autowired
JwtUtil jwtUtil;
	
@GetMapping("/")	
public String HelloWorld() {
	System.out.println("Inside get method");
	return "HelloWorld";
}

@RequestMapping(value="/authenticate", method=RequestMethod.POST)
public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authReq) throws Exception
{
	try {
		System.out.println("Inside the AuthenticationToken");
		authManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUserName(), authReq.getPassword()));
	}
	catch (BadCredentialsException e)
	{
		throw new Exception ("Invalid User Name and password", e);
	}
	// To get the user details to generate JWT
	final UserDetails userDetails = empUserDetails.loadUserByUsername(authReq.getUserName());
	
	//To generate the jwt token
	final String jwtToken = jwtUtil.generateToken(userDetails);
	System.out.println("jwtToken:"+jwtToken);
	
	return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
}
}
