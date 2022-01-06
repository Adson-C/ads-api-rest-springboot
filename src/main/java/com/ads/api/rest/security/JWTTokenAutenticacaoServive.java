package com.ads.api.rest.security;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ads.api.rest.ApplicationContextLoad;
import com.ads.api.rest.model.Usuario;
import com.ads.api.rest.repository.UsuarioRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoServive {

	private static final long EXPIRATION_TIME = 172800000; // experia em 2 dias tempo do token
	
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
		response.addHeader(HEADER_STRING, token); // Authorization: Bearer 80856e6egdfyf7r4
		
		// liberanddo reposta para porta difrentes do uso da API Clientes WEB
		liberacaoCors(response);
		
		// escreve token como resposta no corpo http
		response.getWriter().write("{\"Authorization\": \""+ token +"\"}");
		
	}
	
	/*Retorna o usuário valido com token , se nao retorna null*/
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		String token = request.getHeader(HEADER_STRING); // pega o token enviado no cabeçaho http
		
		try {
		
		if (token != null) {
			
			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
			
			// faz a validação do token do usuário na requisição
			String user = Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(tokenLimpo)
					.getBody().getSubject();
			if (user != null) {
				
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
						.getBean(UsuarioRepository.class).findUserByLogin(user);
				
				if (usuario != null) {
					
					if (tokenLimpo.equalsIgnoreCase(usuario.getToken())) {
						
					
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), 
							usuario.getSenha(), 
							usuario.getAuthorities());
				}
			}

		}

	}
		
		}catch (io.jsonwebtoken.ExpiredJwtException e) {
			try {
				response.getOutputStream().println("Seu Token expirado , atualize o Token.");
			} catch (IOException e1) {}
			
		}
		
		liberacaoCors(response);
	
			return null; // não autorizado
			
		}

	private void liberacaoCors(HttpServletResponse response) {
		
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
		
	}
	
}