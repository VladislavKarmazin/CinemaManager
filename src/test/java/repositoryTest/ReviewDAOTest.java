package repositoryTest;

import entity.Review;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import repository.ConnectionManager;
import repository.ReviewDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReviewDAOTest {
    @Test
    void findReviewsByMovieId() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        try (MockedStatic<ConnectionManager> mockedConnectionManager = mockStatic(ConnectionManager.class)) {
            mockedConnectionManager.when(ConnectionManager::getConnection).thenReturn(mockConnection);

            ReviewDAO reviewDAO = new ReviewDAO();

            Review expectedReview = new Review();
            expectedReview.setReviewId(1);
            expectedReview.setText("Test review text");

            when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("reviewId")).thenReturn(expectedReview.getReviewId());
            when(mockResultSet.getString("text")).thenReturn(expectedReview.getText());

            List<Review> actualReviews = reviewDAO.findReviewsByMovieId(1);

            assertEquals(1, actualReviews.size());
            Review actualReview = actualReviews.get(0);
            assertEquals(expectedReview.getReviewId(), actualReview.getReviewId());
            assertEquals(expectedReview.getText(), actualReview.getText());

            verify(mockConnection).prepareStatement(any());
            verify(mockPreparedStatement).setInt(1, 1);
            verify(mockPreparedStatement).executeQuery();
            verify(mockResultSet, times(2)).next();
            verify(mockResultSet).getInt("reviewId");
            verify(mockResultSet).getString("text");
        }
    }


}
