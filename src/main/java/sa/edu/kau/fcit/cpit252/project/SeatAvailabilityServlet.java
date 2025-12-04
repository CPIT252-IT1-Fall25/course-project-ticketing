package sa.edu.kau.fcit.cpit252.project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/seatavailability")
public class SeatAvailabilityServlet extends HttpServlet {

    private ShowService showService;
    private MovieService movieService;

    @Override
    public void init() throws ServletException {
        this.showService = new ShowService();
        this.movieService = new MovieService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        String movieName = req.getParameter("movieName");
        String location = req.getParameter("location");
        String showTime = req.getParameter("showTime");
        String hallType = req.getParameter("hallType");
        
        PrintWriter out = resp.getWriter();
        
        try {
            if (movieName == null || location == null || showTime == null) {
                out.print("{\"error\": \"Missing required parameters\"}");
                return;
            }
            
            // Get or create movie
            Movie movie = movieService.getMovieByName(movieName);
            if (movie == null) {
                out.print("{\"error\": \"Movie not found\"}");
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
            
            out.print("{\"showId\": " + showId + ", \"availableSeats\": " + availableSeats + "}");
            
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        }
    }
}


