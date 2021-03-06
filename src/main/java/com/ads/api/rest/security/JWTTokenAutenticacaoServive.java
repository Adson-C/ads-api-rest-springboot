package com.ads.api.rest.security;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.ads.api.rest.ApplicationContextLoad;
import com.ads.api.rest.model.Usuario;
import com.ads.api.rest.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Arrays;

@Service
@Component
public class JWTTokenAutenticacaoServive {

	private static final long EXPIRATION_TIME = 518400000; // experia em 2 dias tempo do token
	
	private static final String SECRET = "senhaExtremaSecreta"; // uma senha unica para autenticação
	
	private static final String TOKEN_PREFIX = "Bearer"; // prefixo padrão de token
	
	private static final String HEADER_STRING = "Authorization";
	
	/*Gera Token de autenticacao e add cabeçalho de resp Http*/
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		
		//Montagen do token
		String JWT = Jwts.builder() // chama o gerador de token
				.setSubject(username) // add o usuario
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // tempo de expiração
				.signWith(SignatureAlgorithm.HS512, SECRET).compact(); // compacta algoritimo de geração de senha
		
		/*junta token com prefix*/
		String token = TOKEN_PREFIX + " " + JWT; // Bearer 80856e6egdfyf7r4
		//add no cabeçaho http
		// adiciona token no cabeçalho http
				response.addHeader(HEADER_STRING, token); // Authorization: Bearer 3498hih345jkh345ui53iu5hyi
				
				/*Liberando reposta para porta diferente do projeto Angular*/
				liberacaoCors(response);
				
				// adiciona token como resposta no corpo do http
				response.getWriter().write("{\"Authorization\": \""+ token+ "\"}");
			}
			
			// Retorna o usuário validado com token ou caso não seja válido retorna null
			public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
				
				// Pega o token enviado no cabeçalho Http
				String token = request.getHeader(HEADER_STRING);
				
				if (token != null) {			
					// Faz validação do Token do usuário na requisição
					String user = Jwts.parser()
							.setSigningKey(SECRET)
							.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
							.getBody()
							.getSubject();	
					
					if (user != null) {					
						Usuario usuario = ApplicationContextLoad
								.getApplicationContext()
								.getBean(UsuarioRepository.class)
								.findUserByLogin(user);
						
						if (usuario != null) {
							return new UsernamePasswordAuthenticationToken(
									usuario.getLogin(), 
									usuario.getSenha(), 
									usuario.getAuthorities());
						}
					}
				}	
				liberacaoCors(response);
				
				return null;
			}

			private void liberacaoCors(HttpServletResponse response) {
				if (response.getHeader("Access-Control-Allow-Origin") == null) {
					response.addHeader("Access-Control-Allow-Origin", "*");
				}
				
				
				  if (response.getHeader("Access-Control-Allow-Headers") == null) {
				  response.addHeader("Access-Control-Allow-Headers", "*"); }
				  
				  if (response.getHeader("Access-Control-Request-Headers") == null) {
				  response.addHeader("Access-Control-Request-Headers", "*"); }
				 
				  if (response.getHeader("Access-Control-Allow-Methods") == null) {
						response.addHeader("Access-Control-Allow-Methods", "*");
					}
			}
			 
}