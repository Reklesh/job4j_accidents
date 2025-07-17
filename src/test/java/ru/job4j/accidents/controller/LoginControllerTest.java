package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenLoginWithInvalidCredentialsThenRedirectToLoginWithError() throws Exception {
        mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin()
                        .user("wrongUser")
                        .password("wrongPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    @WithMockUser
    public void whenGetLoginThenReturnsLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"));
    }

    @Test
    @WithMockUser
    public void whenGetLoginWithLogoutParamThenShowLogoutMessage() throws Exception {
        mockMvc.perform(get("/login").param("logout", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"))
                .andExpect(model().attribute("errorMessage",
                        "You have been successfully logged out !!"));
    }

    @Test
    @WithMockUser
    public void whenGetLoginWithErrorParamThenShowErrorMessage() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"))
                .andExpect(model().attribute("errorMessage",
                        "Username or Password is incorrect !!"));
    }

    @Test
    @WithMockUser
    public void whenGetLogoutThenRedirectsToLoginWithLogoutParam() throws Exception {
        mockMvc.perform(get("/logout"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login?logout=true"));
    }
}
