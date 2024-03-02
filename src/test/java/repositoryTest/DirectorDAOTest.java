package repositoryTest;

import entity.Director;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import repository.ConnectionManager;
import repository.DirectorDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DirectorDAOTest {

    @Test
    void findDirectorByMovieId() throws SQLException {
        try (MockedStatic<ConnectionManager> mockedStatic = mockStatic(ConnectionManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedStatic.when(ConnectionManager::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);

            when(mockResultSet.getInt("directorId")).thenReturn(1);
            when(mockResultSet.getString("name")).thenReturn("Test Director");

            DirectorDAO directorDAO = new DirectorDAO();

            Director result = directorDAO.findDirectorByMovieId(1);

            assertEquals(1, result.getDirectorId());
            assertEquals("Test Director", result.getName());

            verify(mockConnection).prepareStatement(any());
            verify(mockPreparedStatement).executeQuery();
            verify(mockResultSet, times(1)).next();
            verify(mockResultSet).getInt("directorId");
            verify(mockResultSet).getString("name");
        }
    }

    @Test
    void findDirectorByName() throws SQLException {
        try (MockedStatic<ConnectionManager> mockedStatic = mockStatic(ConnectionManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockResultSet = mock(ResultSet.class);

            mockedStatic.when(ConnectionManager::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);

            when(mockResultSet.getInt("directorId")).thenReturn(1);
            when(mockResultSet.getString("name")).thenReturn("Test Director");

            DirectorDAO directorDAO = new DirectorDAO();

            Director result = directorDAO.findDirectorByName("Test Director");

            assertEquals(1, result.getDirectorId());
            assertEquals("Test Director", result.getName());

            verify(mockConnection).prepareStatement(any());
            verify(mockPreparedStatement).executeQuery();
            verify(mockResultSet, times(1)).next();
            verify(mockResultSet).getInt("directorId");
            verify(mockResultSet).getString("name");
        }
    }

    @Test
    void addDirector() throws SQLException {
        try (MockedStatic<ConnectionManager> mockedStatic = mockStatic(ConnectionManager.class)) {
            Connection mockConnection = mock(Connection.class);
            PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
            ResultSet mockGeneratedKeys = mock(ResultSet.class);

            mockedStatic.when(ConnectionManager::getConnection).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(any(), anyInt())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Успешное выполнение запроса
            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
            when(mockGeneratedKeys.next()).thenReturn(true);
            when(mockGeneratedKeys.getInt(1)).thenReturn(1); // Задаем ожидаемый сгенерированный ключ

            DirectorDAO directorDAO = new DirectorDAO();

            Director director = new Director();
            director.setName("Test Director");

            directorDAO.addDirector(director);

            verify(mockConnection).prepareStatement(any(), anyInt());
            verify(mockPreparedStatement).setString(1, "Test Director");
            verify(mockPreparedStatement).executeUpdate();
            verify(mockGeneratedKeys, times(1)).next();
            verify(mockGeneratedKeys).getInt(1);

            assertEquals(1, director.getDirectorId());
        }
    }
}
