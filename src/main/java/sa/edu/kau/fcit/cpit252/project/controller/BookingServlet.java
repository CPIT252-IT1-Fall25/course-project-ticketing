package sa.edu.kau.fcit.cpit252.project.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sa.edu.kau.fcit.cpit252.project.model.Movie;
import sa.edu.kau.fcit.cpit252.project.service.BookingService;
import sa.edu.kau.fcit.cpit252.project.service.MovieService;
import sa.edu.kau.fcit.cpit252.project.service.ShowService;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    private BookingService bookingService;
    private ShowService showService;
    private MovieService movieService;

    @Override
    public void init() throws ServletException {
        this.bookingService = new BookingService();
        this.showService = new ShowService();
        this.movieService = new MovieService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        // Get user from session
        HttpSession session = req.getSession();
        String userEmail = (String) session.getAttribute("userEmail");
        
        if (userEmail == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print("{\"error\": \"User not logged in\"}");
            return;
        }
        
        // Get parameters
        String movieName = req.getParameter("movieName");
        String location = req.getParameter("location");
        String showTime = req.getParameter("showTime");
        String hallType = req.getParameter("hallType");
        String ticketType = req.getParameter("ticketType");
        String ticketQuantityStr = req.getParameter("ticketQuantity");
        String popcornQuantityStr = req.getParameter("popcornQuantity");
        String totalPriceStr = req.getParameter("totalPrice");
        
        PrintWriter out = resp.getWriter();
        
        try {
            // Validate parameters
            if (movieName == null || location == null || showTime == null || 
                ticketType == null || ticketQuantityStr == null || totalPriceStr == null) {
                out.print("{\"success\": false, \"error\": \"Missing required parameters\"}");
                return;
            }
            
            int ticketQuantity = Integer.parseInt(ticketQuantityStr);
            int popcornQuantity = popcornQuantityStr != null ? Integer.parseInt(popcornQuantityStr) : 0;
            BigDecimal totalPrice = new BigDecimal(totalPriceStr);
            
            // Get or create movie
            Movie movie = movieService.getMovieByName(movieName);
            if (movie == null) {
                out.print("{\"success\": false, \"error\": \"Movie not found\"}");
                return;
            }
            
            // Get or create show
            int showId = showService.getOrCreateShow(
                movie.getMovieId(), 
                location, 
                showTime, 
                hallType != null ? hallType : "Standard Hall"
            );
            
            // Check available seats before booking
            int availableSeats = showService.getAvailableSeats(showId);
            if (availableSeats < ticketQuantity) {
                out.print("{\"success\": false, \"error\": \"Not enough seats available. Available: " + availableSeats + "\"}");
                return;
            }
            
            // Create booking (this will atomically reserve seats)
            int bookingId = bookingService.createBooking(
                showId,
                userEmail,
                ticketType,
                ticketQuantity,
                popcornQuantity,
                totalPrice
            );
            
            if (bookingId > 0) {
                out.print("{\"success\": true, \"bookingId\": " + bookingId + ", \"message\": \"Booking successful\"}");
            } else {
                out.print("{\"success\": false, \"error\": \"Failed to create booking. Not enough seats available.\"}");
            }
            
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"error\": \"Invalid number format\"}");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"error\": \"Database error: " + e.getMessage() + "\"}");
        }
    }
}


