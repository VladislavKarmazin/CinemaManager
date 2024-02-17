package mapper;

import dto.MovieDTO;
import entity.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class MovieMapper {

    public MovieDTO toDto(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setMovieId(movie.getMovieId());
        movieDTO.setTitle(movie.getTitle());
        // Map other fields from Movie to MovieDTO

        return movieDTO;
    }

    public static Movie toEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setMovieId(movieDTO.getMovieId());
        movie.setTitle(movieDTO.getTitle());
        movie.setYear(movieDTO.getYear());
        movie.setGenre(movieDTO.getGenre());
        movie.setDescription(movieDTO.getDescription());
        return movie;
    }

    public List<MovieDTO> toDtoList(List<Movie> movies) {
        return movies.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
