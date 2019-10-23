package com.semafoor.semaforce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Bootstrap class for the application.
 */
@SpringBootApplication
public class SemaforceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SemaforceApplication.class, args);
	}
}
