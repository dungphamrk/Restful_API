package ra.ss9.service;


import ra.ss9.model.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(LogAnalysisService.class);

    @Autowired
    private MovieService movieService;

    public Map<String, Integer> getSearchKeywords() {
        Map<String, Integer> searchKeywords = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("logs/app.log"))) {
            String line;
            Pattern pattern = Pattern.compile("Search keyword: (.*?),");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String keyword = matcher.group(1);
                    searchKeywords.put(keyword, searchKeywords.getOrDefault(keyword, 0) + 1);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading log file", e);
        }

        return searchKeywords;
    }

    public List<Movie> getMovieSuggestions() {
        Map<String, Integer> searchKeywords = getSearchKeywords();
        Set<Movie> suggestedMovies = new HashSet<>();

        searchKeywords.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> {
                    String keyword = entry.getKey();
                    List<Movie> movies = movieService.searchMovies(keyword);
                    suggestedMovies.addAll(movies);
                });

        return new ArrayList<>(suggestedMovies);
    }
}
