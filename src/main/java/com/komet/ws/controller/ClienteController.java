package com.komet.ws.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.komet.ws.vo.ClienteVO;
import com.komet.ws.vo.MensajeVO;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

@ManagedBean
@ViewScoped
public class ClienteController {
	
	@ManagedProperty(value = "#{clienteVO}")
	private ClienteVO clienteVO;
	
	@ManagedProperty(value = "#{MensajeVO}")
	MensajeVO mensajeVO;
	
	@PostConstruct
	public void inicializar() {
		try {
			clienteVO = new ClienteVO();
		} catch (Exception ex) {
			ex.printStackTrace();
        }
	}
	
	public void save() {
        try {
        	
        	ClientConfig clientConfig = new DefaultClientConfig();
    		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    		Client client = Client.create(clientConfig);
    		WebResource webResource = client.resource("http://localhost:8090/Komet_Rest/services/clienteService/insertCliente");
    		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, clienteVO);

    		mensajeVO = new MensajeVO();
    		mensajeVO = response.getEntity(MensajeVO.class);
        	
    		if(mensajeVO.getTipo().equals("Success"))
    			messageSuccess(mensajeVO.getMensaje());
    		else
    			messageError(mensajeVO.getMensaje());
        	
		} catch (Exception ex) {
			ex.printStackTrace();
        }
    }
	
	public void messageSuccess(String msg){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO!!", msg);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void messageError(String msg){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR!!", msg);
        FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public ClienteVO getClienteVO() {
		return clienteVO;
	}

	public void setClienteVO(ClienteVO clienteVO) {
		this.clienteVO = clienteVO;
	}
	
	public MensajeVO getMensajeVO() {
		return mensajeVO;
	}

	public void setMensajeVO(MensajeVO mensajeVO) {
		this.mensajeVO = mensajeVO;
	}
	
}
