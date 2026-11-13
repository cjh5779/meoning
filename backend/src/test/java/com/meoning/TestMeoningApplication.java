package com.meoning;

import org.springframework.boot.SpringApplication;

public class TestMeoningApplication {

	public static void main(String[] args) {
		SpringApplication.from(MeoningApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
