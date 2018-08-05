package com.escolaRest.javaClient;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class RestResponseExceptionHandler extends DefaultResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		System.out.println("Entrou no hasError");
		return super.hasError(response);
	}
	
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		System.out.println("Fazendo algo com o status code " + response.getStatusCode());
		super.handleError(response);
	}
}
