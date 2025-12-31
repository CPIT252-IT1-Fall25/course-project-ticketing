package sa.edu.kau.fcit.cpit252.project.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
import jakarta.servlet.http.HttpSession;
import sa.edu.kau.fcit.cpit252.project.model.Movie;
import sa.edu.kau.fcit.cpit252.project.service.BookingService;
import sa.edu.kau.fcit.cpit252.project.service.MovieService;
import sa.edu.kau.fcit.cpit252.project.service.PricingService;
import sa.edu.kau.fcit.cpit252.project.service.ShowService;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    private BookingService bookingService;
    private ShowService showService;
    private MovieService movieService;
    private PricingService pricingService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.bookingService = new BookingService();
        this.showService = new ShowService();
        this.movieService = new MovieService();
        this.pricingService = new PricingService();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        // Enable CORS for API access
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        PrintWriter out = resp.getWriter();
        Map<String, Object> response = new HashMap<>();
        
        // Get user from session
        HttpSession session = req.getSession();
        String userEmail = (String) session.getAttribute("userEmail");
        
        if (userEmail == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.put("success", false);
            response.put("error", "User not logged in");
            out.print(gson.toJson(response));
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
        
        try {
            // Validate parameters
            if (movieName == null || location == null || showTime == null || 
                ticketType == null || ticketQuantityStr == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("success", false);
                response.put("error", "Missing required parameters");
                out.print(gson.toJson(response));
                return;
            }
            
            int ticketQuantity = Integer.parseInt(ticketQuantityStr);
            int popcornQuantity = popcornQuantityStr != null ? Integer.parseInt(popcornQuantityStr) : 0;
            
            // CALCULATE PRICE SERVER-SIDE - No longer trust client-provided total
            BigDecimal totalPrice = pricingService.calculateTotalPrice(ticketType, ticketQuantity, popcornQuantity);
            
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
            
            // Check available seats before booking
            int availableSeats = showService.getAvailableSeats(showId);
            if (availableSeats < ticketQuantity) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                response.put("success", false);
                response.put("error", "Not enough seats available");
                response.put("availableSeats", availableSeats);
                response.put("requestedSeats", ticketQuantity);
                out.print(gson.toJson(response));
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
                // Get price breakdown for response
                PricingService.PriceBreakdown breakdown = pricingService.getPriceBreakdown(
                    ticketType, ticketQuantity, popcornQuantity
                );
                
                response.put("success", true);
                response.put("bookingId", bookingId);
                response.put("message", "Booking successful");
                
                // Include pricing details in response
                Map<String, Object> pricing = new HashMap<>();
                pricing.put("ticketUnitPrice", breakdown.getTicketUnitPrice());
                pricing.put("ticketQuantity", breakdown.getTicketQuantity());
                pricing.put("ticketSubtotal", breakdown.getTicketSubtotal());
                pricing.put("popcornUnitPrice", breakdown.getPopcornUnitPrice());
                pricing.put("popcornQuantity", breakdown.getPopcornQuantity());
                pricing.put("popcornSubtotal", breakdown.getPopcornSubtotal());
                pricing.put("discount", breakdown.getDiscount());
                pricing.put("total", breakdown.getTotal());
                response.put("pricing", pricing);
                
                out.print(gson.toJson(response));
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                response.put("success", false);
                response.put("error", "Failed to create booking. Not enough seats available.");
                out.print(gson.toJson(response));
            }
            
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.put("success", false);
            response.put("error", "Invalid number format");
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
        // Handle CORS preflight requests
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}


