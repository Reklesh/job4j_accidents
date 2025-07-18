package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.accident.AccidentService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AccidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
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
    public void whenPostSaveAccidentThenRedirectsToIndexAndSavesAccident() throws Exception {
        this.mockMvc.perform(post("/saveAccident")
                        .param("name", "ДТП")
                        .param("text", "Превышение скорости")
                        .param("address", "ул. Ленина, 5")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));

        ArgumentCaptor<Accident> argument = ArgumentCaptor.forClass(Accident.class);
        verify(accidentService).save(argument.capture());
        Accident accident = argument.getValue();

        assertThat(accident.getName()).isEqualTo("ДТП");
        assertThat(accident.getText()).isEqualTo("Превышение скорости");
        assertThat(accident.getAddress()).isEqualTo("ул. Ленина, 5");
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

    @Test
    @WithMockUser
    public void whenPostUpdateAccidentThenRedirectsToIndexAndUpdatesAccident() throws Exception {
        this.mockMvc.perform(post("/updateAccident")
                        .param("name", "ДТП")
                        .param("text", "Превышение скорости")
                        .param("address", "ул. Ленина, 5")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));

        ArgumentCaptor<Accident> argument = ArgumentCaptor.forClass(Accident.class);
        verify(accidentService).update(argument.capture());
        Accident accident = argument.getValue();

        assertThat(accident.getName()).isEqualTo("ДТП");
        assertThat(accident.getText()).isEqualTo("Превышение скорости");
        assertThat(accident.getAddress()).isEqualTo("ул. Ленина, 5");
    }
}
