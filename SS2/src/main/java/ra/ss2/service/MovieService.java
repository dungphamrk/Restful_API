package ra.ss2.service;
import  ra.ss2.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss2.repository.MovieRepository;

import java.util.List;

@Service
public class MovieService implements IService<Movie, Long> {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public void save(Movie movie) {
        movieRepository.save(movie);
    }


    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    @Override
    public Movie update(Movie movie) {
        if (!movieRepository.existsById(movie.getId())) {
            throw new RuntimeException("Movie not found with id: " + movie.getId());
        }
        return movieRepository.save(movie);
    }

    public List<Movie> findByTitleContainingIgnoreCase(String title){
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }
}