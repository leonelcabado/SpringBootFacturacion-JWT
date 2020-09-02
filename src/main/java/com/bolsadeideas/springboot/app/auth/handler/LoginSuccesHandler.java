package com.bolsadeideas.springboot.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component
public class LoginSuccesHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		SessionFlashMapManager flashManager = new SessionFlashMapManager(); //mapas de errores
		
		FlashMap flashMap = new FlashMap(); //parecido al flash attributte
		
		flashMap.put("success", "Hola, " + authentication.getName()+ "Haz iniciado sesión con éxito!!");
		
		flashManager.saveOutputFlashMap(flashMap, request, response);
		
		if(authentication != null) {
			logger.info("el usuario'"+authentication.getName()+"' se ha logeado con exito!");	
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	

}
