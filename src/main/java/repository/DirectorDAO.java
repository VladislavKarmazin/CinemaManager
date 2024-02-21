package repository;

import config.AppConfig;
import entity.Director;

import java.sql.*;

public class DirectorDAO {
    public Director findDirectorByMovieId(Integer movieId) {
        Director director = null;

        try (Connection connection = ConnectionManager.getConnection()) {
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
        return director;
    }

    private Director mapDirector(ResultSet resultSet) throws SQLException {
        Director director = new Director();
        director.setDirectorId(resultSet.getInt("directorId"));
        director.setName(resultSet.getString("name"));

        return director;
    }
}