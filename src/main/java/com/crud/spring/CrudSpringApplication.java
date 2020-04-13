package com.crud.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableCaching
public class CrudSpringApplication implements  WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(CrudSpringApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("cw2esqu31"));
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/usuario/**")
		.allowedMethods("*")
		.allowedOrigins("*");
		/*Liberando o mapeamento de usuario para todas as origins*/
	}

	

}
