package com.bolsadeideas.springboot.app.auth.filter;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bolsadeideas.springboot.app.auth.service.JWTService;
import com.bolsadeideas.springboot.app.auth.service.JWTServiceImpl;
import com.bolsadeideas.springboot.app.models.entity.Usuario;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	
	private AuthenticationManager authenticationManager; //en los filtros no se pueden inyecctar obj por eso se crean en constructor
	private JWTService jwtService;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		
		if(username != null && password != null) {
			logger.info("Username desde request parameter (from-data): "+ username);
			logger.info("Password desde request parameter (from-data): "+ password);
		}else {
			Usuario user = null;
			try {
				user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class); //desde json a objeto Usuario
				
				username = user.getUsername();
				password = user.getPassword();
				
				logger.info("Username desde request inputStream (raw): "+ username);
				logger.info("Password desde request inputStream (raw): "+ password);
				
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		username = username.trim();
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password); //no es el token de jwt sino es un token de manejo interno de spring security
		
		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String token = jwtService.create(authResult);
		
		response.addHeader(JWTServiceImpl.HEADER_STRING, JWTServiceImpl.TOKEN_PREFIX + token); //de forma standard se usa bearer al inicio, y authorization
		
		Map<String, Object> body = new HashMap<String,Object>();
		body.put("token", token);
		body.put("user", (User)authResult.getPrincipal());
		body.put("mensaje", String.format("Hola %s, has iniciado sesión con éxito!!", ((User)authResult.getPrincipal()).getUsername()));
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body)); //toma el mapa y lo convierte a json con objectMapper
		response.setStatus(200);
		response.setContentType("application/json");
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		Map<String, Object> body = new HashMap<String,Object>();
		body.put("mensaje", "Error de autenticación: username o password incorrectos!!");
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body)); //toma el mapa y lo convierte a json con objectMapper
		response.setStatus(403); //acceso prohibido
		response.setContentType("application/json");
	}
	
	
	
}
