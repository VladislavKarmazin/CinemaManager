package repository;

import config.AppConfig;
import entity.Director;

import java.sql.*;

public class DirectorDAO {
    private static final String URL = AppConfig.getDbUrl();
    private static final String USER = AppConfig.getDbUser();
    private static final String PASSWORD = AppConfig.getDbPassword();

    public Director findDirectorByMovieId(Integer movieId) {
        Director director = null;

        try {
            Class.forName("org.postgresql.Driver");

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String query = "SELECT * FROM directors WHERE directorId = (SELECT directorId FROM movies WHERE movieId = ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, movieId);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            director = mapDirector(resultSet);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return director;
    }

    private Director mapDirector(ResultSet resultSet) throws SQLException {
        Director director = new Director();
        director.setDirectorId(resultSet.getInt("directorId"));
        director.setName(resultSet.getString("name"));

        return director;
    }
}