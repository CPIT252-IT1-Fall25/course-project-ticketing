package sa.edu.kau.fcit.cpit252.project;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import sa.edu.kau.fcit.cpit252.project.controller.BookingServlet;
import sa.edu.kau.fcit.cpit252.project.controller.LoginServlet;
import sa.edu.kau.fcit.cpit252.project.controller.MovieApiServlet;
import sa.edu.kau.fcit.cpit252.project.controller.PriceCalculationServlet;
import sa.edu.kau.fcit.cpit252.project.controller.SeatAvailabilityServlet;
import sa.edu.kau.fcit.cpit252.project.controller.SignupServlet;
import sa.edu.kau.fcit.cpit252.project.controller.UserInfoServlet;

/**
 * Main class to start the embedded Jetty server.
 * Run this class to start the web application.
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        // Get port from environment variable (for cloud deployment) or default to 8080
        int port = System.getenv("PORT") != null 
            ? Integer.parseInt(System.getenv("PORT")) 
            : 8080;
        Server server = new Server(port);
        
        // Create servlet context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        // Register all servlets
        context.addServlet(new ServletHolder(new BookingServlet()), "/booking");
        context.addServlet(new ServletHolder(new PriceCalculationServlet()), "/calculateprice");
        context.addServlet(new ServletHolder(new SeatAvailabilityServlet()), "/seatavailability");
        context.addServlet(new ServletHolder(new MovieApiServlet()), "/movieapi");
        context.addServlet(new ServletHolder(new LoginServlet()), "/login");
        context.addServlet(new ServletHolder(new SignupServlet()), "/signup");
        context.addServlet(new ServletHolder(new UserInfoServlet()), "/userinfo");
        
        // Start server
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║        FAKEnetflix Cinema Booking Server                 ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  Starting server on port " + port + "...                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        server.start();
        
        System.out.println();
        System.out.println("✅ Server started successfully!");
        System.out.println();
        System.out.println("📍 API Endpoints:");
        System.out.println("   • POST /booking          - Create a booking");
        System.out.println("   • GET  /calculateprice   - Calculate ticket prices");
        System.out.println("   • GET  /seatavailability - Check available seats");
        System.out.println("   • GET  /movieapi         - Search movies from OMDB");
        System.out.println("   • POST /login            - User login");
        System.out.println("   • POST /signup           - User registration");
        System.out.println("   • GET  /userinfo         - Get user info");
        System.out.println();
        System.out.println("🌐 Open your HTML files in a browser to use the application.");
        System.out.println("   Example: file:///C:/Users/yazan/OneDrive/Documents/course-project-ticketing/index.html");
        System.out.println();
        System.out.println("🔍 Test movie search API:");
        System.out.println("   http://localhost:" + port + "/movieapi?action=search&keyword=Avengers");
        System.out.println();
        System.out.println("💰 Test price calculation API:");
        System.out.println("   http://localhost:" + port + "/calculateprice?ticketType=pro&ticketQuantity=3&popcornQuantity=2");
        System.out.println();
        System.out.println("Press Ctrl+C to stop the server.");
        System.out.println();
        
        server.join();
    }
}

