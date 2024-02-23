package controller;

import com.google.gson.Gson;
import dto.MovieDTO;
import mapper.MovieMapper;
import repository.DirectorDAO;
import repository.MovieDAO;
import repository.ReviewDAO;
import service.MovieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet("/movies")
public class MoviesServlet extends HttpServlet {

    private final MovieService movieService;
    private final Gson gson;

    public MoviesServlet() {
        this.movieService = new MovieService(new MovieDAO(), new MovieMapper(), new DirectorDAO(), new ReviewDAO());
        this.gson = new Gson();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer movieId = parseIntegerParameter(request.getParameter("movieId"));
        String title = request.getParameter("title");
        Integer directorId = parseIntegerParameter(request.getParameter("directorId"));
        Integer year = parseIntegerParameter(request.getParameter("year"));
        String genre = request.getParameter("genre");
        String description = request.getParameter("description");

        try {
            List<MovieDTO> movies = movieService.findMoviesByParams(movieId, title, directorId, year, genre, description);

            String json = gson.toJson(movies);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        try {
            MovieDTO newMovieDTO = gson.fromJson(requestBody, MovieDTO.class);
            movieService.addMovie(newMovieDTO);

            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        try {
            MovieDTO updatedMovieDTO = gson.fromJson(requestBody, MovieDTO.class);
            movieService.updateMovie(updatedMovieDTO);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");

        try {
            movieService.deleteMovie(title);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private Integer parseIntegerParameter(String parameter) {
        try {
            return Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}