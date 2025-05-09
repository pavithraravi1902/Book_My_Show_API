package com.bookmyshow.controller;

import com.bookmyshow.dto.ShowRequestDTO;
import com.bookmyshow.model.Movie;
import com.bookmyshow.model.Show;
import com.bookmyshow.model.Theatre;
import com.bookmyshow.service.ShowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShowController.class)
@Import(com.bookmyshow.config.SpringSecurityConfig.class)
public class ShowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShowService showService;

    @Autowired
    private ObjectMapper objectMapper;

    private Show sampleShow;

    @BeforeEach
    public void setUp() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Example Movie");
        movie.setGenre("Action");
        movie.setDuration(120);

        Theatre theatre = new Theatre();
        theatre.setId(1L);
        theatre.setName("PVR");
        theatre.setCity("Chennai");

        sampleShow = new Show();
        sampleShow.setId(1L);
        sampleShow.setMovie(movie);
        sampleShow.setTheatre(theatre);
        sampleShow.setShowTime(LocalDateTime.of(2025, 5, 10, 18, 30));
        sampleShow.setPrice(new BigDecimal("150.00"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllShows() throws Exception {
        List<Show> shows = Arrays.asList(sampleShow);
        when(showService.getAllShows()).thenReturn(shows);

        mockMvc.perform(get("/api/shows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetShowById() throws Exception {
        when(showService.getShowById(1L)).thenReturn(sampleShow);

        mockMvc.perform(get("/api/shows/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleShow.getId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateShow() throws Exception {
        ShowRequestDTO dto = new ShowRequestDTO();
        dto.setMovieId(1L);
        dto.setTheatreId(1L);
        dto.setShowTime(LocalDateTime.of(2025, 5, 10, 18, 30));
        dto.setPrice(new BigDecimal("150.00"));

        when(showService.createShow(any(ShowRequestDTO.class))).thenReturn(sampleShow);

        mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleShow.getId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateShow() throws Exception {
        when(showService.updateShow(any(Long.class), any(Show.class))).thenReturn(sampleShow);

        mockMvc.perform(put("/api/shows/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleShow)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleShow.getId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteShow() throws Exception {
        doNothing().when(showService).deleteShow(1L);

        mockMvc.perform(delete("/api/shows/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "pavithra", roles = {"USER"})
    public void testAccessDeniedForUserRole() throws Exception {
        mockMvc.perform(get("/api/shows"))
                .andExpect(status().isForbidden());
    }
}
