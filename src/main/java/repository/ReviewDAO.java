package repository;

import config.AppConfig;
import entity.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private static final String URL = AppConfig.getDbUrl();
    private static final String USER = AppConfig.getDbUser();
    private static final String PASSWORD = AppConfig.getDbPassword();

    public List<Review> findReviewsByMovieId(Integer movieId) {
        List<Review> reviews = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM reviews WHERE movieId = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, movieId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Review review = mapReview(resultSet);
                        reviews.add(review);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    private Review mapReview(ResultSet resultSet) throws SQLException {
        Review review = new Review();
        review.setReviewId(resultSet.getInt("reviewId"));
        review.setText(resultSet.getString("text"));
        // Добавьте другие поля, если они есть

        return review;
    }
}
