package com.bookmyshow.controller;

import com.bookmyshow.model.Movie;
import com.bookmyshow.service.MovieService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllMovies() throws Exception {
        Movie movie1 = new Movie(1L, "Interstellar", "Sci-fi", "English", 169, "Epic space film", "https://poster.com/interstellar.jpg");
        Movie movie2 = new Movie(2L, "Bahubali", "Action", "Telugu", 160, "Indian epic action", "https://poster.com/bahubali.jpg");

        when(movieService.getAllMovies()).thenReturn(Arrays.asList(movie1, movie2));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Interstellar"))
                .andExpect(jsonPath("$[1].language").value("Telugu"));
    }

    @Test
    public void testGetMovieById_whenMovieExists_shouldReturnMovie() throws Exception {
        Movie movie = new Movie(1L, "Joker", "Drama", "English", 122, "Psychological drama", "https://poster.com/joker.jpg");

        when(movieService.getMovieById(1L)).thenReturn(Optional.of(movie));

        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Joker"))
                .andExpect(jsonPath("$.duration").value(122));
    }

    @Test
    public void testGetMovieById_whenMovieNotFound_shouldReturn404() throws Exception {
        when(movieService.getMovieById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/movies/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateMovie() throws Exception {
        Movie newMovie = new Movie(null, "Leo", "Thriller", "Tamil", 140, "Action-packed Tamil film", "https://poster.com/leo.jpg");
        Movie savedMovie = new Movie(10L, "Leo", "Thriller", "Tamil", 140, "Action-packed Tamil film", "https://poster.com/leo.jpg");

        when(movieService.addMovie(any(Movie.class))).thenReturn(savedMovie);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Leo"));
    }

    @Test
    public void testUpdateMovie() throws Exception {
        Long movieId = 3L;
        Movie updateData = new Movie(null, "KGF Chapter 2", "Action", "Kannada", 160, "Sequel to KGF", "https://poster.com/kgf2.jpg");
        Movie updatedMovie = new Movie(3L, "KGF Chapter 2", "Action", "Kannada", 160, "Sequel to KGF", "https://poster.com/kgf2.jpg");

        when(movieService.updateMovie(eq(movieId), any(Movie.class))).thenReturn(updatedMovie);

        mockMvc.perform(put("/api/movies/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("KGF Chapter 2"));
    }

    @Test
    public void testDeleteMovieById_shouldReturnOk() throws Exception {
        Long movieId = 7L;
        doNothing().when(movieService).deleteMovie(movieId);
        mockMvc.perform(delete("/api/movies/{id}", movieId))
                .andExpect(status().isOk());
        verify(movieService, times(1)).deleteMovie(movieId);
    }
}
