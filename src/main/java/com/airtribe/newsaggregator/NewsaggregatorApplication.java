package com.airtribe.newsaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class NewsaggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsaggregatorApplication.class, args);
	}

}
