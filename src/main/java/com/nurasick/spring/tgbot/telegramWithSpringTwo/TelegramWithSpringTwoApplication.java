package com.nurasick.spring.tgbot.telegramWithSpringTwo;

import com.nurasick.spring.tgbot.telegramWithSpringTwo.utils.RestTemplateModified;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TelegramWithSpringTwoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramWithSpringTwoApplication.class, args);
	}

	@Bean
	public RestTemplateModified restTemplate() {
		return new RestTemplateModified();
	}

}
