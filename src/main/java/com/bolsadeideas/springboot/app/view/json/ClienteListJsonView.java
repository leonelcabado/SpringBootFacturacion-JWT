package com.bolsadeideas.springboot.app.view.json;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("listar.json")
public class ClienteListJsonView extends MappingJackson2JsonView {

	@Override
	@SuppressWarnings("unchecked")
	protected Object filterModel(Map<String, Object> model) {
		// TODO Auto-generated method stub
		
		model.remove("titulo");
		model.remove("page");
		
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		
		model.remove("clientes");
		
		model.put("clienteList", clientes.getContent()); //dentro de la clase wrapper ClienteList, lo paso en formato List
		
		return super.filterModel(model);
	}

}
