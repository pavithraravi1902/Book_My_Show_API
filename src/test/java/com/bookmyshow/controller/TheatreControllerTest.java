package com.bookmyshow.controller;

import com.bookmyshow.model.Theatre;
import com.bookmyshow.service.TheatreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TheatreControllerTest {

    @Mock
    private TheatreService theatreService;

    @InjectMocks
    private TheatreController theatreController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(theatreController).build();
    }

    private Theatre getSampleTheatre() {
        Theatre theatre = new Theatre();
        theatre.setId(101L);
        theatre.setName("INOX Chennai");
        theatre.setCity("Chennai");
        theatre.setAddress("Express Avenue Mall, Royapettah");
        return theatre;
    }

    @Test
    public void testGetAllTheatres() throws Exception {
        Theatre theatre = getSampleTheatre();
        when(theatreService.getAllTheatres()).thenReturn(Arrays.asList(theatre));
        mockMvc.perform(get("/api/theatres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("INOX Chennai"))
                .andExpect(jsonPath("$[0].city").value("Chennai"))
                .andExpect(jsonPath("$[0].address").value("Express Avenue Mall, Royapettah"));
    }

    @Test
    public void testGetTheatreById() throws Exception {
        Theatre theatre = getSampleTheatre();
        when(theatreService.getTheatreById(101L)).thenReturn(theatre);
        mockMvc.perform(get("/api/theatres/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("INOX Chennai"))
                .andExpect(jsonPath("$.city").value("Chennai"))
                .andExpect(jsonPath("$.address").value("Express Avenue Mall, Royapettah"));
    }

    @Test
    public void testAddTheatre() throws Exception {
        Theatre theatre = getSampleTheatre();
        when(theatreService.addTheatre(any(Theatre.class))).thenReturn(theatre);
        String json = new ObjectMapper().writeValueAsString(theatre);
        mockMvc.perform(post("/api/theatres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("INOX Chennai"))
                .andExpect(jsonPath("$.city").value("Chennai"))
                .andExpect(jsonPath("$.address").value("Express Avenue Mall, Royapettah"));
    }

    @Test
    public void testUpdateTheatre() throws Exception {
        Theatre updated = getSampleTheatre();
        updated.setName("PVR Phoenix");
        when(theatreService.updateTheatre(eq(101L), any(Theatre.class))).thenReturn(updated);
        String json = new ObjectMapper().writeValueAsString(updated);
        mockMvc.perform(put("/api/theatres/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PVR Phoenix"));
    }

    @Test
    public void testDeleteTheatre() throws Exception {
        doNothing().when(theatreService).deleteTheatre(101L);
        mockMvc.perform(delete("/api/theatres/101"))
                .andExpect(status().isOk());
        verify(theatreService, times(1)).deleteTheatre(101L);
    }
}
