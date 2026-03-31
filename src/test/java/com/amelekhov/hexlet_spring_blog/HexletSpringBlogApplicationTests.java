package com.amelekhov.hexlet_spring_blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class HexletSpringBlogApplicationTests {

	@Test
	void contextLoads() {
	}

}
