package com.bolsadeideas.springboot.app.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.bolsadeideas.springboot.app.auth.service.JWTService;
import com.bolsadeideas.springboot.app.auth.service.JWTServiceImpl;



public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private JWTService jwtService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		String header = request.getHeader(JWTServiceImpl.HEADER_STRING);

		if (!requieresAuthentication(header)) { //si el metodo es falso ejecuta el doFilter
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = null;
		if(jwtService.validate(header)) {	
			authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header), null , jwtService.getRoles(header)); //guardo info de token en authentication
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication); //valido authentication
		chain.doFilter(request, response); //sigo con los demas filtros
	}
	
	protected boolean requieresAuthentication(String header) {
		if (header == null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX)) { //consulto si no hay token en encabezado o si no existe Bearer al comienzo del token
			return false;
		}
		return true;
	}

}
