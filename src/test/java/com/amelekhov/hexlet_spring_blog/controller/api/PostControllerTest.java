package com.amelekhov.hexlet_spring_blog.controller.api;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(PostController.class)
@ActiveProfiles("test")
public class PostControllerTest {
}
