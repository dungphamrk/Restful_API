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

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @GetMapping("/search-logs")
    public ResponseEntity<Map<String, Long>> getSearchLogs() {
        try {
            Map<String, Long> searchCounts = new HashMap<>();
            String logFilePath = "app.log";
            Pattern pattern = Pattern.compile("Search keyword: ([^,]+),");

            try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String keyword = matcher.group(1).trim();
                        searchCounts.merge(keyword, 1L, Long::sum);
                    }
                }
            }

            logger.info("Retrieved search logs, Total unique keywords: {}", searchCounts.size());
            return ResponseEntity.ok(searchCounts);
        } catch (Exception e) {
            logger.error("Error retrieving search logs: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<Movie>> getMovieSuggestions() {
        try {
            Map<String, Long> searchCounts = new HashMap<>();
            String logFilePath = "app.log";
            Pattern pattern = Pattern.compile("Search keyword: ([^,]+),");

            // Read search keywords from log file
            try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String keyword = matcher.group(1).trim();
                        searchCounts.merge(keyword, 1L, Long::sum);
                    }
                }
            }

            // Get top 3 most searched keywords
            List<String> topKeywords = searchCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // Get movies matching top keywords
            List<Movie> suggestions = new ArrayList<>();
            for (String keyword : topKeywords) {
                List<Movie> movies = movieService.searchMovies(keyword);
                suggestions.addAll(movies);
            }

            // Remove duplicates while preserving order
            suggestions = suggestions.stream()
                    .distinct()
                    .collect(Collectors.toList());

            logger.info("Retrieved movie suggestions, Total suggestions: {}", suggestions.size());
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            logger.error("Error retrieving movie suggestions: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}