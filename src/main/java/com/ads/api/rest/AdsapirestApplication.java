package com.ads.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"com.ads.api.rest.model"})
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = {"com.ads.api.rest.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
public class AdsapirestApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(AdsapirestApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("123"));
	}
	
	/*Mapeamento Global */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		/*liberando todos os End-point (/**)  allowedMethods("POST", "PUT", "GET", "DELETE") */ 
		registry.addMapping("/usuario/**")
		.allowedMethods("*")
		.allowedOrigins("*");
	}

}
