package com.liu.deal;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class DealApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DealApplication.class, args);
	}

	@Bean
	public Queue Queue() {
		return new Queue("hello");
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
