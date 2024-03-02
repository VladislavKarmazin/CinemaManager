package repository;

import entity.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public List<Review> findReviewsByMovieId(Integer movieId) {
        List<Review> reviews = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection()) {
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

        return review;
    }
}