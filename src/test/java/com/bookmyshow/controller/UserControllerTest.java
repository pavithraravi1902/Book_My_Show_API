package com.bookmyshow.controller;

import com.bookmyshow.model.User;
import com.bookmyshow.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content("{ \"email\": \"test@example.com\", \"password\": \"password\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testLoginValid() throws Exception {
        when(userService.validateLogin("test@example.com", "password")).thenReturn(true);

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{ \"email\": \"test@example.com\", \"password\": \"password\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    public void testLoginInvalid() throws Exception {
        when(userService.validateLogin("test@example.com", "wrongpassword")).thenReturn(false);

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{ \"email\": \"test@example.com\", \"password\": \"wrongpassword\" }"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("book@show.com");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("book@show.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("bookmy@show.com");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType("application/json")
                        .content("{ \"email\": \"bookmy@show.com\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("bookmy@show.com"));
    }

    @Test
    public void testLoginThrowsException() throws Exception {
        when(userService.validateLogin(anyString(), anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{ \"email\": \"bookmy@show.com\", \"password\": \"password\" }"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unexpected error"));
    }

    @Test
    public void testLoginRuntimeException() throws Exception {
        when(userService.validateLogin(anyString(), anyString()))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{ \"email\": \"book@example.com\", \"password\": \"password\" }"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Service error"));
    }
}
