package com.bolsadeideas.springboot.app.view.xml;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("listar.xml") //corresponde al listar del controller cliente
public class ClienteListXmlView extends MarshallingView{
	
	@Autowired
	public ClienteListXmlView(Jaxb2Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		model.remove("titulo");
		model.remove("page");
		
		
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		
		model.remove("clientes");
		
		model.put("clienteList", new ClienteList(clientes.getContent())); //dentro de la clase wrapper ClienteList, lo paso en formato List
		
		super.renderMergedOutputModel(model, request, response);
	}
	
}
