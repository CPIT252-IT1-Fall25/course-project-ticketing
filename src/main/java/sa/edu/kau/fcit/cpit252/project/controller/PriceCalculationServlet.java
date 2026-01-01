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
import sa.edu.kau.fcit.cpit252.project.service.PricingService;

/**
 * Servlet that calculates ticket prices server-side.
 * This ensures pricing logic is controlled by the backend, not the client.
 */
@WebServlet("/calculateprice")
public class PriceCalculationServlet extends HttpServlet {

    private PricingService pricingService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.pricingService = new PricingService();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        // Enable CORS
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        PrintWriter out = resp.getWriter();
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Get parameters
            String ticketType = req.getParameter("ticketType");
            String ticketQuantityStr = req.getParameter("ticketQuantity");
            String popcornQuantityStr = req.getParameter("popcornQuantity");
            
            // Validate required parameters
            if (ticketType == null || ticketQuantityStr == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("success", false);
                response.put("error", "Missing required parameters: ticketType and ticketQuantity");
                out.print(gson.toJson(response));
                return;
            }
            
            int ticketQuantity = Integer.parseInt(ticketQuantityStr);
            int popcornQuantity = popcornQuantityStr != null ? Integer.parseInt(popcornQuantityStr) : 0;
            
            // Validate quantities
            if (ticketQuantity < 0 || popcornQuantity < 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("success", false);
                response.put("error", "Quantities cannot be negative");
                out.print(gson.toJson(response));
                return;
            }
            
            // Calculate price breakdown
            PricingService.PriceBreakdown breakdown = pricingService.getPriceBreakdown(
                ticketType, ticketQuantity, popcornQuantity
            );
            
            response.put("success", true);
            response.put("ticketType", ticketType);
            response.put("ticketUnitPrice", breakdown.getTicketUnitPrice());
            response.put("ticketQuantity", breakdown.getTicketQuantity());
            response.put("ticketSubtotal", breakdown.getTicketSubtotal());
            response.put("popcornUnitPrice", breakdown.getPopcornUnitPrice());
            response.put("popcornQuantity", breakdown.getPopcornQuantity());
            response.put("popcornSubtotal", breakdown.getPopcornSubtotal());
            response.put("discount", breakdown.getDiscount());
            response.put("discountApplied", breakdown.getDiscount().compareTo(java.math.BigDecimal.ZERO) > 0);
            response.put("bulkDiscountThreshold", PricingService.BULK_DISCOUNT_THRESHOLD);
            response.put("total", breakdown.getTotal());
            response.put("currency", "SAR");
            
            out.print(gson.toJson(response));
            
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.put("success", false);
            response.put("error", "Invalid number format for quantity");
            out.print(gson.toJson(response));
        }
    }
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}


