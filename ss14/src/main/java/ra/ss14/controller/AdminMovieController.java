package ra.ss14.controller;

import ra.ss14.model.dto.request.MovieRequestDTO;
import ra.ss14.model.dto.response.APIResponse;
import ra.ss14.model.entity.Movie;
import ra.ss14.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/movies")
@RequiredArgsConstructor
public class AdminMovieController{
    private final MovieService movieService;

    @PostMapping
    private ResponseEntity<APIResponse<Movie>> saveMovie(@Valid @RequestBody MovieRequestDTO movieRequestDTO){
        Movie movie = movieService.addMovie(movieRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new APIResponse<>(true, "Movie created successfully", movie, HttpStatus.CREATED)
        );
    }

    @PutMapping("{id}")
    private ResponseEntity<APIResponse<Movie>> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequestDTO movieRequestDTO){
        Movie movie = movieService.updateMovie(id, movieRequestDTO);
        return ResponseEntity.ok(
                new APIResponse<>(true, "Movie updated successfully", movie, HttpStatus.OK)
        );
    }

    @DeleteMapping("{id}")
    private ResponseEntity<APIResponse<Movie>> deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
        return ResponseEntity.ok(
                new APIResponse<>(true, "Movie deleted successfully", null, HttpStatus.NO_CONTENT)
        );
    }
}