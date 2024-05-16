package com.picpaysimplificado.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpaysimplificado.domain.User;
import com.picpaysimplificado.domain.UserType;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Autowired
    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    @DisplayName("Test controller method create")
    void postCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO(
                "John",
                "Doe",
                "123456789",
                new BigDecimal("1000.00"),
                "john.doe@example.com",
                "password",
                UserType.REGULAR
        );

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDocument("123456789");
        user.setBalance(new BigDecimal("1000.00"));
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setUserType(UserType.REGULAR);

        when(userService.createUser(userDTO)).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void getAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setDocument("123456789");
        user1.setBalance(new BigDecimal("1000.00"));
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password");
        user1.setUserType(UserType.REGULAR);

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setDocument("987654321");
        user2.setBalance(new BigDecimal("2000.00"));
        user2.setEmail("jane.doe@example.com");
        user2.setPassword("password2");
        user2.setUserType(UserType.PREMIUM);

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }
}