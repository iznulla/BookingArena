package com.booking.arena;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArenaApplication {
	Logger logger = LoggerFactory.getLogger(ArenaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ArenaApplication.class, args);
	}

}
