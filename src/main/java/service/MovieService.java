package service;

import dto.MovieDTO;
import entity.Director;
import entity.Movie;
import entity.Review;
import mapper.DirectorMapper;
import mapper.MovieMapper;
import repository.DirectorDAO;
import repository.MovieDAO;
import repository.ReviewDAO;

import java.util.List;

public class MovieService {

    private final MovieDAO movieDAO;
    private final MovieMapper movieMapper;
    private final DirectorDAO directorDAO;
    private final ReviewDAO reviewDAO;


    public MovieService(MovieDAO movieDAO, MovieMapper movieMapper, DirectorDAO directorDAO, ReviewDAO reviewDAO) {
        this.movieDAO = movieDAO;
        this.movieMapper = movieMapper;
        this.directorDAO = directorDAO;
        this.reviewDAO = reviewDAO;
    }

    public List<MovieDTO> findMoviesByParams(Integer movieId, String title, Integer directorId, Integer year, String genre, String description) {
        List<Movie> movies = movieDAO.findMoviesByParams(movieId, title, directorId, year, genre, description);

        for (Movie movie : movies) {
            try {
                Director director = directorDAO.findDirectorByMovieId(movie.getMovieId());
                movie.setDirector(director);

                List<Review> reviews = reviewDAO.findReviewsByMovieId(movie.getMovieId());
                movie.setReviews(reviews);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return movieMapper.toDtoList(movies);
    }

    public void addMovie(MovieDTO movieDTO) {
        Movie movie = MovieMapper.toEntity(movieDTO);

        if (movieDTO.getDirector() != null) {
            Director director = DirectorMapper.toEntity(movieDTO.getDirector());
            Director existingDirector = directorDAO.findDirectorByName(director.getName());

            if (existingDirector != null) {
                movie.setDirector(existingDirector);
            } else {
                directorDAO.addDirector(director);
                movie.setDirector(director);
            }
        }

        movieDAO.addMovie(movie);
    }

    public void updateMovie(MovieDTO movieDTO) {
        Movie movie = MovieMapper.toEntity(movieDTO);

        if (movieDTO.getDirector() != null) {
            Director director = DirectorMapper.toEntity(movieDTO.getDirector());
            Director existingDirector = directorDAO.findDirectorByName(director.getName());

            if (existingDirector != null) {
                movie.setDirector(existingDirector);
            } else {
                directorDAO.addDirector(director);
                movie.setDirector(director);
            }
        }

        movieDAO.updateMovie(movie);
    }

    public void deleteMovie(String title) {
        movieDAO.deleteMovie(title);
    }
}