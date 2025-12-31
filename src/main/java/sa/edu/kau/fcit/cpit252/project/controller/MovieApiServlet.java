package sa.edu.kau.fcit.cpit252.project.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sa.edu.kau.fcit.cpit252.project.service.MovieApiService;

/**
 * Servlet that provides movie information from external OMDB API.
 * Demonstrates integration with external REST APIs.
 */
@WebServlet("/movieapi")
public class MovieApiServlet extends HttpServlet {

    private MovieApiService movieApiService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.movieApiService = new MovieApiService();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        // Enable CORS
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        PrintWriter out = resp.getWriter();
        Map<String, Object> response = new HashMap<>();
        
        String action = req.getParameter("action");
        String title = req.getParameter("title");
        String imdbId = req.getParameter("imdbId");
        String keyword = req.getParameter("keyword");
        
        try {
            if (action == null) {
                action = "search";
            }
            
            switch (action) {
                case "details":
                    // Get detailed movie info by title
                    if (title != null && !title.trim().isEmpty()) {
                        MovieApiService.MovieInfo movie = movieApiService.searchMovie(title);
                        if (movie != null) {
                            response.put("success", true);
                            response.put("movie", movie);
                        } else {
                            response.put("success", false);
                            response.put("error", "Movie not found");
                        }
                    } else if (imdbId != null && !imdbId.trim().isEmpty()) {
                        MovieApiService.MovieInfo movie = movieApiService.getMovieById(imdbId);
                        if (movie != null) {
                            response.put("success", true);
                            response.put("movie", movie);
                        } else {
                            response.put("success", false);
                            response.put("error", "Movie not found");
                        }
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.put("success", false);
                        response.put("error", "Please provide title or imdbId parameter");
                    }
                    break;
                    
                case "search":
                    // Search for movies
                    String searchTerm = keyword != null ? keyword : title;
                    if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                        MovieApiService.MovieSearchResult result = movieApiService.searchMovies(searchTerm);
                        if (result.isSuccess()) {
                            response.put("success", true);
                            response.put("totalResults", result.getTotalResults());
                            response.put("movies", result.getSearch());
                        } else {
                            response.put("success", false);
                            response.put("error", "No movies found");
                        }
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.put("success", false);
                        response.put("error", "Please provide keyword or title parameter");
                    }
                    break;
                    
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.put("success", false);
                    response.put("error", "Invalid action. Use 'search' or 'details'");
            }
            
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.put("success", false);
            response.put("error", "Failed to fetch movie data: " + e.getMessage());
        }
        
        out.print(gson.toJson(response));
    }
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}

