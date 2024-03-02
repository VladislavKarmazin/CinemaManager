package repositoryTest;

import entity.Movie;
import entity.Review;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import repository.ConnectionManager;
import repository.MovieDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieDAOTest {

    @Test
    void findMoviesByParams() throws SQLException {
        try (MockedStatic<ConnectionManager> mockedConnectionManager = mockStatic(ConnectionManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedConnectionManager.when(ConnectionManager::getConnection).thenReturn(mockConnection);

            MovieDAO movieDAO = spy(new MovieDAO());

            when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);

            when(mockResultSet.getInt("movieId")).thenReturn(1);
            when(mockResultSet.getString("title")).thenReturn("Test Movie");
            when(mockResultSet.getInt("year")).thenReturn(2022);
            when(mockResultSet.getString("genre")).thenReturn("Test Genre");
            when(mockResultSet.getString("description")).thenReturn("Test Description");

            doReturn(null).when(movieDAO).findDirectorByMovieId(anyInt());
            doReturn(null).when(movieDAO).findReviewsByMovieId(anyInt());

            List<Movie> result = movieDAO.findMoviesByParams(1, "Test Movie", 1, 2022, "Test Genre", "Test Description");

            assertNotNull(result);
            assertEquals(1, result.size());
            Movie movie = result.get(0);
            assertEquals(1, movie.getMovieId());
            assertEquals("Test Movie", movie.getTitle());
            assertEquals(2022, movie.getYear());
            assertEquals("Test Genre", movie.getGenre());
            assertEquals("Test Description", movie.getDescription());

            movie.setReviews(new ArrayList<>());

            assertNotNull(movie.getReviews());
            assertTrue(movie.getReviews().isEmpty());
        }
    }

    @Test
    void findReviewsByMovieId() throws SQLException {
        try (MockedStatic<ConnectionManager> mockedConnectionManager = mockStatic(ConnectionManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedConnectionManager.when(ConnectionManager::getConnection).thenReturn(mockConnection);

            MovieDAO movieDAO = new MovieDAO();

            when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);

            when(mockResultSet.getInt("reviewId")).thenReturn(1);
            when(mockResultSet.getString("text")).thenReturn("Test Review");

            List<Review> result = movieDAO.findReviewsByMovieId(1);

            assertNotNull(result);
            assertEquals(1, result.size());
            Review review = result.get(0);
            assertEquals(1, review.getReviewId());
            assertEquals("Test Review", review.getText());

            verify(mockConnection).prepareStatement(any());
            verify(mockPreparedStatement).executeQuery();
            verify(mockResultSet, times(2)).next();
            verify(mockResultSet).getInt("reviewId");
            verify(mockResultSet).getString("text");
        }
    }

    @Test
    void addMovie() throws SQLException {
        try (MockedStatic<ConnectionManager> mockedConnectionManager = mockStatic(ConnectionManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockGeneratedKeys = mock(ResultSet.class);

            mockedConnectionManager.when(ConnectionManager::getConnection).thenReturn(mockConnection);

            MovieDAO movieDAO = new MovieDAO();

            when(mockConnection.prepareStatement(any(), anyInt())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);
            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
            when(mockGeneratedKeys.next()).thenReturn(true);
            when(mockGeneratedKeys.getInt(1)).thenReturn(1);

            Movie movie = new Movie();
            movie.setTitle("Test Movie");
            movie.setYear(2022);
            movie.setGenre("Test Genre");
            movie.setDescription("Test Description");

            movieDAO.addMovie(movie);

            verify(mockConnection).prepareStatement(any(), anyInt());
            verify(mockPreparedStatement).setString(1, "Test Movie");
            verify(mockPreparedStatement).setInt(2, 2022);
            verify(mockPreparedStatement).setString(3, "Test Genre");
            verify(mockPreparedStatement).setString(4, "Test Description");
            verify(mockPreparedStatement).setNull(5, Types.INTEGER);
            verify(mockPreparedStatement).executeUpdate();
            verify(mockGeneratedKeys).next();
            verify(mockGeneratedKeys).getInt(1);
        }
    }

    @Test
    void updateMovie() throws SQLException {
        try (MockedStatic<ConnectionManager> mockedConnectionManager = mockStatic(ConnectionManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            mockedConnectionManager.when(ConnectionManager::getConnection).thenReturn(mockConnection);

            MovieDAO movieDAO = new MovieDAO();

            when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            Movie movie = new Movie();
            movie.setMovieId(1);
            movie.setTitle("Updated Movie");
            movie.setYear(2023);
            movie.setGenre("Updated Genre");
            movie.setDescription("Updated Description");

            movieDAO.updateMovie(movie);

            verify(mockConnection).prepareStatement(any());
            verify(mockPreparedStatement).setString(1, "Updated Movie");
            verify(mockPreparedStatement).setInt(2, 2023);
            verify(mockPreparedStatement).setString(3, "Updated Genre");
            verify(mockPreparedStatement).setString(4, "Updated Description");
            verify(mockPreparedStatement).setNull(5, Types.INTEGER);
            verify(mockPreparedStatement).setInt(6, 1);
            verify(mockPreparedStatement).executeUpdate();
        }
    }

    @Test
    void deleteMovie() throws SQLException {
        try (MockedStatic<ConnectionManager> mockedConnectionManager = mockStatic(ConnectionManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

            mockedConnectionManager.when(ConnectionManager::getConnection).thenReturn(mockConnection);

            MovieDAO movieDAO = new MovieDAO();

            when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            movieDAO.deleteMovie("Test Movie");

            verify(mockConnection).prepareStatement(any());
            verify(mockPreparedStatement).setString(1, "Test Movie");
            verify(mockPreparedStatement).executeUpdate();
        }
    }
}
