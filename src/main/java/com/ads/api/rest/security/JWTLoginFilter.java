package com.ads.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ads.api.rest.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;

/*Estabelece o nosso gerecador de token*/
public class JWTLoginFilter  extends AbstractAuthenticationProcessingFilter{

	// config o geren de autenticação
	protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(url)); // obriga a autenticar a URL
		
		setAuthenticationManager(authenticationManager); // gerenciador de autenticação
		
		
		
	}
	
	/*Retorna o usuário ao processar a autenticação*/
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		Usuario user = new ObjectMapper() // pega o token p/ validar
				.readValue(request.getInputStream(), Usuario.class);
		
		return getAuthenticationManager() // Retorna o usario login, senha e acesso
				.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		new JWTTokenAutenticacaoServive().addAuthentication(response, authResult.getName());
	}

}
