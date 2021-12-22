package com.ads.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JWTApiAutenticacaoFilter extends GenericFilterBean { // Filtro requisições serão captiradas

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		/*autenticaçao para requisição */
		Authentication authentication = new JWTTokenAutenticacaoServive().getAuthentication((HttpServletRequest)request);
		
		SecurityContextHolder.getContext().setAuthentication(authentication); // colocar o processo de autenticação no spring security
		
		chain.doFilter(request, response);
	}

}
