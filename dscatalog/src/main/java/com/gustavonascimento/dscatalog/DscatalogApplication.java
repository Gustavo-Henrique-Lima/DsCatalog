package com.gustavonascimento.dscatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API para loja 'DSCatalog'", version = "1.0", description = "API criada para o projeto de um catalogo de produtos criado durante o bootcamp Spring 3.0 da escola de programação DevSuperior."))
public class DscatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(DscatalogApplication.class, args);
	}

}
