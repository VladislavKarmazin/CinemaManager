package repository;

import config.AppConfig;
import entity.Director;
import entity.Movie;
import entity.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Слой Data Access Object для фильтрации запросов
 */
public class MovieDAO {
    private static final String URL = AppConfig.getDbUrl();
    private static final String USER = AppConfig.getDbUser();
    private static final String PASSWORD = AppConfig.getDbPassword();

    public List<Movie> findMoviesByParams(Integer movieId, String title, Integer directorId, Integer year, String genre, String description) {
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            StringBuilder query = new StringBuilder("SELECT * FROM movies WHERE 0=0");

            if (movieId != null) query.append(" AND movieId = ?");
            if (title != null) query.append(" AND title = ?");
            if (directorId != null) query.append(" AND directorId = ?");
            if (year != null) query.append(" AND year = ?");
            if (genre != null) query.append(" AND genre = ?");
            if (description != null) query.append(" AND description = ?");

            try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
                int parameterIndex = 1;

                if (movieId != null) statement.setInt(parameterIndex++, movieId);
                if (title != null) statement.setString(parameterIndex++, title);
                if (directorId != null) statement.setInt(parameterIndex++, directorId);
                if (year != null) statement.setInt(parameterIndex++, year);
                if (genre != null) statement.setString(parameterIndex++, genre);
                if (description != null) statement.setString(parameterIndex++, description);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Movie movie = new Movie();
                        movie.setMovieId(resultSet.getInt("movieId"));
                        movie.setTitle(resultSet.getString("title"));
                        movie.setYear(resultSet.getInt("year"));
                        movie.setGenre(resultSet.getString("genre"));
                        movie.setDescription(resultSet.getString("description"));

                        // Загружаем режиссера для фильма
                        Director director = findDirectorByMovieId(movie.getMovieId());
                        movie.setDirector(director);

                        // Загружаем рецензии для фильма
                        List<Review> reviews = findReviewsByMovieId(movie.getMovieId());
                        movie.setReviews(reviews);

                        movies.add(movie);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    private Director findDirectorByMovieId(Integer movieId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM directors WHERE movieId = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, movieId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Director director = new Director();
                        director.setDirectorId(resultSet.getInt("directorId"));
                        director.setName(resultSet.getString("name"));
                        return director;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Review> findReviewsByMovieId(Integer movieId) {
        List<Review> reviews = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM reviews WHERE movieId = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, movieId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Review review = new Review();
                        review.setReviewId(resultSet.getInt("reviewId"));
                        review.setText(resultSet.getString("text"));
                        reviews.add(review);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public void addMovie(Movie movie) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO movies (title, director, year, genre, description) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, movie.getTitle());
                statement.setInt(3, movie.getYear());
                statement.setString(4, movie.getGenre());
                statement.setString(5, movie.getDescription());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMovie(Movie movie) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "UPDATE movies SET title = ?, director = ?, year = ?, genre = ?, description = ? WHERE movieId = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, movie.getTitle());
                statement.setInt(3, movie.getYear());
                statement.setString(4, movie.getGenre());
                statement.setString(5, movie.getDescription());
                statement.setInt(6, movie.getMovieId());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(Integer movieId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "DELETE FROM movies WHERE movieId = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, movieId);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}