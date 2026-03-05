package com.example.Shop.it;

import com.example.Shop.entity.User;
import com.example.Shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldCreateUser() throws Exception {
        String requestJson = """
                {
                    "name":"Marcel",
                    "email":"marcelo@yahoo.com"
                }
                """;

        mockMvc.perform(  post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson)  )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Marcel"))
                .andExpect(jsonPath("$.email").value("marcelo@yahoo.com"));
    }

    @Test
    void shouldCreateUserResultVersion() throws Exception{
        String requestJson = """
                {
                    "name":"Marcel",
                    "email":"marcelo@yahoo.com"
                }
                """;

        ResultActions resultActions =  mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        resultActions.andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value("Marcel"))
                .andExpect(jsonPath("$.email").value("marcelo@yahoo.com"));
    }

    @Test
    void shouldReturnUserById() throws Exception{
        User user = new User(null, "Marcela", "marcela@yahoo.com", null);
        userRepository.save(user);

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Marcela"))
                .andExpect(jsonPath("$.email").value("marcela@yahoo.com"));
    }

    @Test
    void shouldReturnListOfUsers() throws Exception{
        userRepository.saveAll(List.of(
                new User(null, "Ana", "ana@gmail.com", null),
                new User(null, "John", "john@gmail.com", null)
                ));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }


    @Test
    void shouldUpdateUser() throws Exception{
        User user = userRepository.save(new User(null,"Old","old@gmail.com",null));

        String updateDto = """
                {
                    "name":"New",
                    "email":"new@gmail.com"
                }
                """;

        mockMvc.perform(put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateDto)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void shouldDeleteUser() throws Exception{
        User user = userRepository.save(new User(null,"Old","old@gmail.com",null));
        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isOk());
//        mockMvc.perform(get("/api/users"))
//                .andExpect(jsonPath("$", hasSize(0)));
    }




}
