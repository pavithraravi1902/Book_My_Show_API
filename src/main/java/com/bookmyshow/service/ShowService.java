package com.bookmyshow.service;

import com.bookmyshow.dto.ShowRequestDTO;
import com.bookmyshow.model.Movie;
import com.bookmyshow.model.Show;
import com.bookmyshow.model.Theatre;
import com.bookmyshow.repository.MovieRepository;
import com.bookmyshow.repository.ShowRepository;
import com.bookmyshow.repository.TheatreRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id " + id));
    }

    public Show createShow(ShowRequestDTO showRequest) {
        Movie movie = movieRepository.findById(showRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + showRequest.getMovieId()));

        Theatre theatre = theatreRepository.findById(showRequest.getTheatreId())
                .orElseThrow(() -> new RuntimeException("Theatre not found with ID: " + showRequest.getTheatreId()));

        Show show = new Show();
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowTime(showRequest.getShowTime());
        show.setPrice(BigDecimal.valueOf(showRequest.getPrice()));

        return showRepository.save(show);
    }


    public Show updateShow(Long id, Show showDetails) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found with id " + id));

        Long movieId = showDetails.getMovie().getId();
        Long theatreId = showDetails.getTheatre().getId();

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with id " + movieId));
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre not found with id " + theatreId));

        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowTime(showDetails.getShowTime());
        show.setPrice(showDetails.getPrice());

        return showRepository.save(show);
    }

    public void deleteShow(Long id) {
        showRepository.deleteById(id);
    }
}
