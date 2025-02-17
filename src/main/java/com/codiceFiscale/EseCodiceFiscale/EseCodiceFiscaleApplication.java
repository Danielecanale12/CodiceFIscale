package com.codiceFiscale.EseCodiceFiscale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"Controller"})
@SpringBootApplication
public class EseCodiceFiscaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(EseCodiceFiscaleApplication.class, args);
	}

}
