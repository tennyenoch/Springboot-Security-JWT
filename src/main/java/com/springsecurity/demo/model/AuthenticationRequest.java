package com.springsecurity.demo.model;

public class AuthenticationRequest {
private String UserName;
private String password;
public String getUserName() {
	return UserName;
}
public void setUserName(String userName) {
	this.UserName = userName;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public AuthenticationRequest(String userName, String password) {
	super();
	this.UserName = userName;
	this.password = password;
}

public AuthenticationRequest()
{
	
}

}
