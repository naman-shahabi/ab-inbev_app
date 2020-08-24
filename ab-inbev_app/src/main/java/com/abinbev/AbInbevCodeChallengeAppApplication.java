package com.abinbev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AbInbevCodeChallengeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbInbevCodeChallengeAppApplication.class, args);
	}

}
