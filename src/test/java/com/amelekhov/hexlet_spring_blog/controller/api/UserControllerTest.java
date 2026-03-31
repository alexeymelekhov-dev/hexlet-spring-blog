package com.amelekhov.hexlet_spring_blog.controller.api;

import com.amelekhov.hexlet_spring_blog.model.User;
import com.amelekhov.hexlet_spring_blog.repository.UserRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Faker faker;

    private User firstUser;
    private User secondUser;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        firstUser = Instancio.of(User.class)
                .ignore(field(User::getId))
                .ignore(field(User.class, "posts"))
                .supply(field(User::getEmail), () -> faker.internet().emailAddress())
                .create();

        secondUser = Instancio.of(User.class)
                .ignore(field(User::getId))
                .ignore(field(User.class, "posts"))
                .supply(field(User::getEmail), () -> faker.internet().emailAddress())
                .create();

        userRepository.saveAll(List.of(firstUser, secondUser));
    }

    @Test
    void getUsers_returns200_andBody() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value(firstUser.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(firstUser.getLastName()))
                .andExpect(jsonPath("$[0].email").value(firstUser.getEmail()))
                .andExpect(jsonPath("$[1].firstName").value(secondUser.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(secondUser.getLastName()))
                .andExpect(jsonPath("$[1].email").value(secondUser.getEmail()));
    }

    @Test
    void createUser_returns201_andBody() throws Exception {
        var body = """
            {
                "firstName": "John",
                "lastName": "Doe",
                "email": "john@example.com"
            }
        """;

        mockMvc.perform(post("/api/users")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void createUser_invalidEmail_returns200() throws Exception {
        var body = """
            {
                "firstName": "John",
                "lastName": "Doe",
                "email": ""
            }
        """;

        mockMvc.perform(post("/api/users")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(body))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    void getUser_response200_andBody() throws Exception {
        mockMvc.perform(get("/api/users/" + firstUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(firstUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(firstUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(firstUser.getLastName()));
    }

    @Test
    void getUser_notExisting_returns404() throws Exception {
        mockMvc.perform(get("/api/users/9999"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void updateUser_success_returns200() throws Exception {
        var body = """
            {
                "firstName": "UpdatedFirst",
                "lastName": "UpdatedLast",
                "email": "updated@example.com"
            }
        """;

        mockMvc.perform(put("/api/users/" + firstUser.getId())
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(body))
                .andExpect(status().isOk());

        var updatedUser = userRepository.findById(firstUser.getId())
                .orElseThrow(() -> new AssertionError("User not found"));

        assertThat(updatedUser.getFirstName()).isEqualTo("UpdatedFirst");
        assertThat(updatedUser.getLastName()).isEqualTo("UpdatedLast");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void  deleteUser_success_returns200() throws Exception {
        mockMvc.perform(delete("/api/users/" + firstUser.getId()))
                .andExpect(status().isNoContent());

        var userFromDb = userRepository.findById(firstUser.getId());
        assertThat(userFromDb).isEmpty();
    }
}
