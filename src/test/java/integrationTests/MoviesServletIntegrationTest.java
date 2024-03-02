package integrationTests;

import config.AppConfig;
import dto.DirectorDTO;
import dto.MovieDTO;
import dto.ReviewDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {AppConfig.class, SpringContextTest.class})
class MoviesServletIntegrationTest {

    @Autowired
    private TestRestTemplate template;

    private final String URL = "http://localhost:8080/CinemaManager/movies";

    @Test
    public void testDoGet_FindMoviesByParams() {
        ResponseEntity<List<MovieDTO>> response = template.exchange(
                RequestEntity.get(URL)
                        .build(),
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpServletResponse.SC_OK, response.getStatusCode().value());

        List<MovieDTO> movies = response.getBody();
        assertNotNull(movies);
        assertFalse(movies.isEmpty());

        MovieDTO anyMovie = movies.get(0);
        DirectorDTO director = anyMovie.getDirector();
        List<ReviewDTO> reviews = anyMovie.getReviews();
        assertFalse(reviews.isEmpty());
        ReviewDTO anyReview = reviews.get(0);

        assertAll(
                () -> assertNotNull(anyMovie.getMovieId()),
                () -> assertNotNull(anyMovie.getTitle()),
                () -> assertNotNull(anyMovie.getYear()),
                () -> assertNotNull(anyMovie.getGenre()),
                () -> assertNotNull(anyMovie.getDescription()),
                () -> assertNotNull(anyMovie.getDirector()),
                () -> assertNotNull(anyMovie.getReviews()),

                () -> assertNotNull(director.getDirectorId()),
                () -> assertNotNull(director.getName()),

                () -> assertNotNull(anyReview.getReviewId()),
                () -> assertNotNull(anyReview.getText())
        );

    }

    @Test
    public void testDoPost_addMovie() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("фильм DoPost");
        movieDTO.setYear(2023);
        movieDTO.setGenre("жанр DoPost");
        movieDTO.setDescription("Описание DoPost");
        movieDTO.setDirector(new DirectorDTO("test Director DoPost"));

        ResponseEntity<MovieDTO> response = template.exchange(
                RequestEntity.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(movieDTO),
                MovieDTO.class
        );

        assertEquals(HttpServletResponse.SC_CREATED, response.getStatusCode().value());
    }

    @Test
    public void testDoPut_SuccessfulUpdate() {
        int movieId = 1;

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setMovieId(movieId);
        movieDTO.setTitle("фильм DoPut");
        movieDTO.setYear(2024);
        movieDTO.setGenre("жанр DoPut");
        movieDTO.setDescription("описание DoPut");
        movieDTO.setDirector(new DirectorDTO( "test Director doPut"));

        ResponseEntity<MovieDTO> responsePut = template.exchange(
                RequestEntity.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(movieDTO),
                MovieDTO.class
        );

        assertEquals(HttpServletResponse.SC_OK, responsePut.getStatusCode().value());

        //Получение doGet запроса для проверки обновленной сущности
        ResponseEntity<List<MovieDTO>> responseGet = template.exchange(
                RequestEntity.get(URL + "?movieId=" + movieId)
                        .build(),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpServletResponse.SC_OK, responseGet.getStatusCode().value());

        List<MovieDTO> updatedMovies = responseGet.getBody();
        assertNotNull(updatedMovies);
        assertFalse(updatedMovies.isEmpty());

        MovieDTO updatedMovie = updatedMovies.get(0);
        assertAll(
                () -> assertEquals(1, updatedMovie.getMovieId()),
                () -> assertEquals("фильм DoPut", updatedMovie.getTitle()),
                () -> assertEquals(2024, updatedMovie.getYear()),
                () -> assertEquals("жанр DoPut", updatedMovie.getGenre()),
                () -> assertEquals("описание DoPut", updatedMovie.getDescription()),
                () -> assertEquals(updatedMovie.getDirector().getName(), "test Director doPut")
        );
    }


    @Test
    void testDoDelete() {
        String title = "Фильм doDelete";

        //Пост запрос сущности, которую удаляем, что бы  не модифицировать БД
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle(title);
        movieDTO.setYear(2023);
        movieDTO.setGenre("Жанр doDelete");
        movieDTO.setDescription("Описание doDelete");

        ResponseEntity<MovieDTO> responsePost = template.exchange(
                RequestEntity.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(movieDTO),
                MovieDTO.class
        );

        assertEquals(HttpServletResponse.SC_CREATED, responsePost.getStatusCode().value());

        //Удаление
        ResponseEntity<List<MovieDTO>> responseDelete = template.exchange(
                RequestEntity.delete(URL + "?title=" + title)
                        .build(),
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpServletResponse.SC_OK, responseDelete.getStatusCode().value());
    }
}
