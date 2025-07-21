package ra.ss9.controller;

import ra.ss9.service.CloudinaryService;
import ra.ss9.service.MovieService;
import ra.ss9.model.DTO.MovieDTO;
import ra.ss9.model.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<Movie> addMovie(@ModelAttribute MovieDTO movieDTO) {
        try {
            Movie movie = new Movie();
            movie.setTitle(movieDTO.getTitle());
            movie.setDescription(movieDTO.getDescription());
            movie.setReleaseDate(movieDTO.getReleaseDate());

            if (movieDTO.getPosterFile() != null && !movieDTO.getPosterFile().isEmpty()) {
                String posterUrl = cloudinaryService.uploadFile(movieDTO.getPosterFile());
                movie.setPoster(posterUrl);
            }

            Movie savedMovie = movieService.saveMovie(movie);
            logger.info("Movie added successfully: {} at {}",
                    savedMovie.getTitle(), LocalDateTime.now());
            return ResponseEntity.ok(savedMovie);
        } catch (Exception e) {
            logger.error("Error adding movie: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @ModelAttribute MovieDTO movieDTO) {
        try {
            Optional<Movie> oldMovieOpt = movieService.getMovieById(id);
            if (oldMovieOpt.isEmpty()) {
                logger.error("Movie not found with id: {}", id);
                return ResponseEntity.notFound().build();
            }

            Movie oldMovie = oldMovieOpt.get();
            Movie updatedMovie = new Movie();
            updatedMovie.setId(id);
            updatedMovie.setTitle(movieDTO.getTitle());
            updatedMovie.setDescription(movieDTO.getDescription());
            updatedMovie.setReleaseDate(movieDTO.getReleaseDate());

            if (movieDTO.getPosterFile() != null && !movieDTO.getPosterFile().isEmpty()) {
                String posterUrl = cloudinaryService.uploadFile(movieDTO.getPosterFile());
                updatedMovie.setPoster(posterUrl);
            } else {
                updatedMovie.setPoster(oldMovie.getPoster());
            }

            Movie result = movieService.updateMovie(id, updatedMovie);

            if (result != null) {
                logger.info("Movie updated successfully:\nOld info: {}\nNew info: {}",
                        oldMovie.toString(), result.toString());
                return ResponseEntity.ok(result);
            } else {
                logger.error("Failed to update movie with id: {}", id);
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            logger.error("Error updating movie: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        try {
            Optional<Movie> movieOpt = movieService.getMovieById(id);
            if (movieOpt.isEmpty()) {
                logger.error("Movie not found with id: {}", id);
                return ResponseEntity.notFound().build();
            }

            Movie movie = movieOpt.get();
            boolean deleted = movieService.deleteMovie(id);

            if (deleted) {
                logger.info("Movie deleted successfully: {}", movie.toString());
                return ResponseEntity.ok().build();
            } else {
                logger.error("Failed to delete movie with id: {}", id);
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting movie: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies(@RequestParam(required = false) String searchMovie) {
        try {
            long startTime = System.currentTimeMillis();
            List<Movie> movies;

            if (searchMovie != null && !searchMovie.trim().isEmpty()) {
                movies = movieService.searchMovies(searchMovie);
                logger.info("Search keyword: {}, Results count: {}, Execution time: {} ms",
                        searchMovie, movies.size(), System.currentTimeMillis() - startTime);
            } else {
                movies = movieService.getAllMovies();
                logger.info("Get all movies, Results count: {}, Execution time: {} ms",
                        movies.size(), System.currentTimeMillis() - startTime);
            }

            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            logger.error("Error getting movies: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
