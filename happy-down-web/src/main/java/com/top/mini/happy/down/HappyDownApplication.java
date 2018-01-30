package com.top.mini.happy.down;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;

/**
 * @author xugang
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class HappyDownApplication {

	public static void main(String[] args) {
		SpringApplication.run(HappyDownApplication.class, args);
	}
}
