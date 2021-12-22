package com.ads.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ads.api.rest.service.ImplUserDetailsService;



@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{ // Mapeia URL, endereço, autoriza ou bloqueia acesso a URL

	@Autowired
	private ImplUserDetailsService implUserServ;
	
	
	/*configura as solicitações de acesso por Http */
	@Override
		protected void configure(HttpSecurity http) throws Exception {
		
		/*Ativando a protecao de usuario  sem validações por Token */
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.disable().authorizeRequests().antMatchers("/").permitAll() // ativando a permissão acesso a pagina inicial do sistema
		.antMatchers("/index").permitAll()
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index") // URL de logout- redireciona após user deslogado
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Maperia URL de logout e insValido o usuario
		/*Filtra requisições de login para autenticação*/
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), 
				UsernamePasswordAuthenticationFilter.class)
		/*Filtra demais requisições para verificar a presenção do Token JWT*/
		.addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		
		
	}
	
	
	@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		/*Service que ira consultar usuario non banco de dados*/
		auth.userDetailsService(implUserServ)
		.passwordEncoder(new BCryptPasswordEncoder());
		
		}
	
}
