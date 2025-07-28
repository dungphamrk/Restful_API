package ra.ss14.service.impl;

import ra.ss14.model.dto.request.MovieRequestDTO;
import ra.ss14.model.entity.Movie;
import ra.ss14.repository.MovieRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.ss14.service.MovieService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepo movieRepo;

    @Override
    public List<Movie> getAllMovies(){
        return movieRepo.findAll();
    }

    @Override
    public Movie addMovie(MovieRequestDTO movieRequestDTO){
        if(movieRepo.existsByTitle(movieRequestDTO.getTitle())){
            throw new RuntimeException("Movie with title " + movieRequestDTO.getTitle() + " already exists");
        }
        return movieRepo.save(Movie.builder()
                .title(movieRequestDTO.getTitle())
                .description(movieRequestDTO.getDescription())
                .duration(movieRequestDTO.getDuration())
                .releaseDate(LocalDate.parse(movieRequestDTO.getReleaseDate()))
                .build());
    }

    @Override
    public Movie updateMovie(Long id, MovieRequestDTO movieRequestDTO){
        Movie movie = movieRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie with id " + id + " not found"));
        movie.setTitle(movieRequestDTO.getTitle());
        movie.setDescription(movieRequestDTO.getDescription());
        movie.setDuration(movieRequestDTO.getDuration());
        movie.setReleaseDate(LocalDate.parse(movieRequestDTO.getReleaseDate()));
        return movieRepo.save(movie);
    }


    @Override
    public void deleteMovie(Long id){
        if(!movieRepo.existsById(id)){
            throw new RuntimeException("Movie with id " + id + " not found");
        }
        movieRepo.deleteById(id);
    }
}