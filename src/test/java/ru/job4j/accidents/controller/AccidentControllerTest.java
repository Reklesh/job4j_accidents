package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.accident.AccidentService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AccidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccidentService accidentService;

    @Test
    @WithMockUser
    public void whenGetCreateAccidentThenReturnsCreateAccidentView() throws Exception {
        mockMvc.perform(get("/createAccident"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accidents/createAccident"));
    }

    @Test
    @WithMockUser
    public void whenGetFormUpdateAccidentThenReturnsEditAccidentView() throws Exception {
        Accident accident = new Accident();
        accident.setId(1);
        accident.setName("Test Accident");

        when(accidentService.findById(1)).thenReturn(Optional.of(accident));

        mockMvc.perform(get("/formUpdateAccident").param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accidents/editAccident"))
                .andExpect(model().attributeExists("accident"));
    }
}
