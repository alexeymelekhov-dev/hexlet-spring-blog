package com.amelekhov.hexlet_spring_blog;

import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class HexletSpringBlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(HexletSpringBlogApplication.class, args);
	}

	@Bean
	public Faker faker() {
		return new Faker();
	}
}