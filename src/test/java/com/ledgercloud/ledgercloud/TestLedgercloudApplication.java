package com.ledgercloud.ledgercloud;

import org.springframework.boot.SpringApplication;

public class TestLedgercloudApplication {

	public static void main(String[] args) {
		SpringApplication.from(LedgercloudApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
