package com.github.lucasyukio.caseitau;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Transfer API", description = "API for registering and searching for clients as well as transfers", version = "V1"))
@SpringBootApplication
public class CaseItauApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaseItauApplication.class, args);
	}

}
