package com.mercadolibre.prueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mercadolibre.prueba")
public class MlApplication {

	public static void main(String[] args) {
		SpringApplication.run(MlApplication.class, args);
	}

}
