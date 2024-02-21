package repository;

import config.AppConfig;
import entity.Director;
import entity.Movie;
import entity.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    public List<Movie> findMoviesByParams(Integer movieId, String title, Integer directorId, Integer year, String genre, String description) {
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection()) {
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

                        Director director = findDirectorByMovieId(movie.getMovieId());
                        movie.setDirector(director);

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
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM directors WHERE directorId IN (SELECT directorId FROM movies WHERE movieId = ?)";

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

        try (Connection connection = ConnectionManager.getConnection()) {
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
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "INSERT INTO movies (title, year, genre, description, directorId) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, movie.getTitle());
                statement.setInt(2, movie.getYear());
                statement.setString(3, movie.getGenre());
                statement.setString(4, movie.getDescription());

                if (movie.getDirector() != null) {
                    statement.setInt(5, movie.getDirector().getDirectorId());
                } else {
                    statement.setNull(5, Types.INTEGER);
                }

                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedMovieId = generatedKeys.getInt(1);
                        movie.setMovieId(generatedMovieId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMovie(Movie movie) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "UPDATE movies SET title = ?, year = ?, genre = ?, description = ?, directorId = ? WHERE movieId = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, movie.getTitle());
                statement.setInt(2, movie.getYear());
                statement.setString(3, movie.getGenre());
                statement.setString(4, movie.getDescription());

                // Устанавливаем directorId в зависимости от наличия режиссера
                if (movie.getDirector() != null) {
                    statement.setInt(5, movie.getDirector().getDirectorId());
                } else {
                    statement.setNull(5, Types.INTEGER);
                }

                statement.setInt(6, movie.getMovieId());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(Integer movieId) {
        try (Connection connection = ConnectionManager.getConnection()) {
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
