package com.springsecurity.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springsecurity.demo.configurer.EmpUserDetailsService;
import com.springsecurity.demo.util.JwtUtil;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private EmpUserDetailsService empUserDetails;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		final String authHeader = request.getHeader("Authorization");
		String userName = null;
		String jwt = null;
		
		// retrieving the jwt and userName from the given JWt token sent from rest api
		if (authHeader != null && authHeader.startsWith("Bearer "))
		{
			jwt = authHeader.substring(7);
			userName = jwtUtil.extractUsername(jwt);
		}
		
		//Validating the token retrieved from Rest call with the token which is already generated.
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null )
		{
			UserDetails userDetails = this.empUserDetails.loadUserByUsername(userName);
			if (jwtUtil.validateToken(jwt, userDetails))
			{
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));	
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	

}
