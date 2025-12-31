package sa.edu.kau.fcit.cpit252.project.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sa.edu.kau.fcit.cpit252.project.model.Movie;
import sa.edu.kau.fcit.cpit252.project.service.MovieService;
import sa.edu.kau.fcit.cpit252.project.service.ShowService;

/**
 * Servlet for checking seat availability for a show.
 * Returns JSON response with available seat count.
 */
@WebServlet("/seatavailability")
public class SeatAvailabilityServlet extends HttpServlet {

    private ShowService showService;
    private MovieService movieService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.showService = new ShowService();
        this.movieService = new MovieService();
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
        
        String movieName = req.getParameter("movieName");
        String location = req.getParameter("location");
        String showTime = req.getParameter("showTime");
        String hallType = req.getParameter("hallType");
        
        PrintWriter out = resp.getWriter();
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (movieName == null || location == null || showTime == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("success", false);
                response.put("error", "Missing required parameters: movieName, location, showTime");
                out.print(gson.toJson(response));
                return;
            }
            
            // Get or create movie
            Movie movie = movieService.getMovieByName(movieName);
            if (movie == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.put("success", false);
                response.put("error", "Movie not found");
                out.print(gson.toJson(response));
                return;
            }
            
            // Get or create show
            int showId = showService.getOrCreateShow(
                movie.getMovieId(), 
                location, 
                showTime, 
                hallType != null ? hallType : "Standard Hall"
            );
            
            // Get available seats
            int availableSeats = showService.getAvailableSeats(showId);
            
            response.put("success", true);
            response.put("showId", showId);
            response.put("movieName", movie.getMovieName());
            response.put("location", location);
            response.put("showTime", showTime);
            response.put("availableSeats", availableSeats);
            out.print(gson.toJson(response));
            
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.put("success", false);
            response.put("error", "Database error: " + e.getMessage());
            out.print(gson.toJson(response));
        }
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


