package com.bolsadeideas.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.Usuario;

@Service("jpaUserDetailsSerivce")
public class JpaUserDetailsService implements UserDetailsService {// no se genera interface ya que la interface la provee spring security para utilizar jpa para el
																	// login 
	@Autowired
	private IUsuarioDao usuarioDao;

	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioDao.findByUsername(username); // obtengo usuario

		if (usuario == null) {
			logger.error("error login: no existe el usuario");
			throw new UsernameNotFoundException("Username" + username + " no existe en el sistema!");
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Role role : usuario.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getAuthority())); // recorro los roles del usuario
		}

		if (authorities.isEmpty()) {
			logger.error("error login: usuario no tiene roles");
			throw new UsernameNotFoundException("Username no tiene roles");
		}

		return new User(username, usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities); // devuelve  un user detail que es el autenticado
	}
}
