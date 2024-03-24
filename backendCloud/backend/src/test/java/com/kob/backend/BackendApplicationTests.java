package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("123456"));
		System.out.println(passwordEncoder.matches("123456","$2a$10$JUMgptjVN2PGVKRC8PeMW.1nHyhXuIzGfXJfJ5RU00R/g8bFuEkai"));
	}

}
