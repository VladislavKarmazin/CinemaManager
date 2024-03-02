package serviceTest;

import dto.MovieDTO;
import entity.Movie;
import mapper.MovieMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.DirectorDAO;
import repository.MovieDAO;
import service.MovieService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieDAO movieDAO;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private DirectorDAO directorDAO;

    @InjectMocks
    private MovieService movieService;

    @Test
    void testFindMoviesByParams() {
        Movie movie = new Movie();
        movie.setMovieId(1);
        movie.setTitle("Title");

        List<Movie> movies = new ArrayList<>();
        movies.add(movie);

        when(movieDAO.findMoviesByParams(null, null, null, null, null, null)).thenReturn(movies);

        List<MovieDTO> expected = new ArrayList<>();
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setMovieId(1);
        movieDTO.setTitle("Title");
        expected.add(movieDTO);

        when(movieMapper.toDtoList(movies)).thenReturn(expected);

        List<MovieDTO> actual = movieService.findMoviesByParams(null, null, null, null, null, null);

        assertEquals(expected, actual);
    }

    @Test
    void testAddMovie() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("Test Movie");
        movieDTO.setYear(2022);

        Movie expectedMovie = new Movie();
        expectedMovie.setTitle("Test Movie");
        expectedMovie.setYear(2022);

        try (MockedStatic<MovieMapper> mockedStatic = mockStatic(MovieMapper.class)) {
            mockedStatic.when(() -> MovieMapper.toEntity(movieDTO)).thenReturn(expectedMovie);

            movieService.addMovie(movieDTO);

            verify(movieDAO).addMovie(expectedMovie);
            verify(directorDAO, never()).addDirector(any());
        }
    }

    @Test
    void testUpdateMovie() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setMovieId(1);
        movieDTO.setTitle("New title");

        Movie movie = new Movie();
        movie.setMovieId(1);
        movie.setTitle("New title");

        try (MockedStatic<MovieMapper> mockedStatic = mockStatic(MovieMapper.class)) {
            mockedStatic.when(() -> MovieMapper.toEntity(movieDTO)).thenReturn(movie);

            movieService.updateMovie(movieDTO);

            verify(movieDAO).updateMovie(movie);
        }
    }

    @Test
    void testDeleteMovie() {
        String title = "Title";

        movieService.deleteMovie(title);

        verify(movieDAO).deleteMovie(title);
    }
}