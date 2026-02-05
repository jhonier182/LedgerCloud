package com.ledgercloud.ledgercloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ledgercloud")
public class LedgercloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(LedgercloudApplication.class, args);
	}

}
