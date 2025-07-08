package ra.ss2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.ss2.entity.Movie;
import ra.ss2.service.MovieService;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public String showMovies(Model model) {
        model.addAttribute("movies",  movieService.findAll());
        return "movie-list";
    }
    @GetMapping("/add")
    public String showAddMovie(Model model) {
        model.addAttribute("movie",  new Movie());
        return "movie-add";
    }

    @PostMapping("/add")
    public String addMovie(@ModelAttribute("movie") Movie movie) {
        movieService.save(movie);
        return "redirect:/movies";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Movie movie = movieService.findById(id);
        if (movie == null) {
            return "redirect:/movies";
        }
        model.addAttribute("movie", movie);
        return "movie-edit";
    }

    @PostMapping("/edit/{id}")
    public String editMovie(@PathVariable("id") Long id, @ModelAttribute("movie") Movie updatedMovie) {
        updatedMovie.setId(id);
        movieService.update(updatedMovie);
        return "redirect:/movies";
    }
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") Long id) {
        movieService.delete(id);
        return "redirect:/movies";
    }
}
