package com.bolsadeideas.springboot.app;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	/*
	 * private final Logger log = LoggerFactory.getLogger(getClass());
	 * 
	 * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
	 * TODO Auto-generated method stub
	 * WebMvcConfigurer.super.addResourceHandlers(registry);
	 * 
	 * String resourcePath =
	 * Paths.get("uploads").toAbsolutePath().toUri().toString();
	 * log.info(resourcePath);
	 * 
	 * registry.addResourceHandler("/uploads/**")
	 * .addResourceLocations(resourcePath);
	 * 
	 * }
	 */

	// Mapeao una ruta a una vista de error sin lógica de negocio, solo mapeo (view
	// controller)

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error_403");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Multilenguaje, se guarda en sessionLocale

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("es", "ES"));
		return localeResolver;
	}

	// Interceptor para detectar lenguaje

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");// cada vez que se pase el parametro por url el parametro lang se
		return localeChangeInterceptor; // ejecuta este interceptor
	}

	// Registro interceptor

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(localeChangeInterceptor()); // registro el interceptor
	}
		
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(new Class[] {com.bolsadeideas.springboot.app.view.xml.ClienteList.class}); //convierte nuestras clases en xml, serializa
		return marshaller;
	}

}
