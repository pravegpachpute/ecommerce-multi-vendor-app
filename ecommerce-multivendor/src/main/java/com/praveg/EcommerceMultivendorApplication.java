package com.praveg;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceMultivendorApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(EcommerceMultivendorApplication.class);
		app.setBannerMode(Banner.Mode.CONSOLE);
		app.run(args);
	}

}
