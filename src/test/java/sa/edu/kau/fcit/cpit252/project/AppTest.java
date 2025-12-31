package sa.edu.kau.fcit.cpit252.project;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import sa.edu.kau.fcit.cpit252.project.service.PricingService;
import sa.edu.kau.fcit.cpit252.project.store.DatabaseConnection;

/**
 * Unit tests for the Cinema Booking Application.
 * Tests pricing calculations, singleton pattern, and service logic.
 */
public class AppTest {

    /**
     * Tests for the PricingService class.
     */
    @Nested
    @DisplayName("PricingService Tests")
    class PricingServiceTests {
        
        private PricingService pricingService;
        
        @BeforeEach
        void setUp() {
            pricingService = new PricingService();
        }
        
        @Test
        @DisplayName("Regular ticket price should be 30 SAR")
        void testRegularTicketPrice() {
            BigDecimal price = pricingService.getTicketPrice("regular");
            assertEquals(new BigDecimal("30.00"), price);
        }
        
        @Test
        @DisplayName("PRO ticket price should be 40 SAR")
        void testProTicketPrice() {
            BigDecimal price = pricingService.getTicketPrice("pro");
            assertEquals(new BigDecimal("40.00"), price);
        }
        
        @Test
        @DisplayName("PRO ticket type should be case insensitive")
        void testProTicketCaseInsensitive() {
            assertEquals(pricingService.getTicketPrice("PRO"), pricingService.getTicketPrice("pro"));
            assertEquals(pricingService.getTicketPrice("Pro"), pricingService.getTicketPrice("pro"));
        }
        
        @Test
        @DisplayName("Unknown ticket type defaults to regular price")
        void testUnknownTicketTypeDefaultsToRegular() {
            BigDecimal price = pricingService.getTicketPrice("unknown");
            assertEquals(new BigDecimal("30.00"), price);
        }
        
        @ParameterizedTest
        @DisplayName("Calculate total for regular tickets without popcorn")
        @CsvSource({
            "regular, 1, 0, 30.00",
            "regular, 2, 0, 60.00",
            "regular, 3, 0, 90.00",
            "regular, 4, 0, 120.00"
        })
        void testRegularTicketsNoPopcorn(String ticketType, int tickets, int popcorn, String expectedTotal) {
            BigDecimal total = pricingService.calculateTotalPrice(ticketType, tickets, popcorn);
            assertEquals(new BigDecimal(expectedTotal), total);
        }
        
        @ParameterizedTest
        @DisplayName("Calculate total for PRO tickets without popcorn")
        @CsvSource({
            "pro, 1, 0, 40.00",
            "pro, 2, 0, 80.00",
            "pro, 3, 0, 120.00",
            "pro, 4, 0, 160.00"
        })
        void testProTicketsNoPopcorn(String ticketType, int tickets, int popcorn, String expectedTotal) {
            BigDecimal total = pricingService.calculateTotalPrice(ticketType, tickets, popcorn);
            assertEquals(new BigDecimal(expectedTotal), total);
        }
        
        @Test
        @DisplayName("Popcorn should cost 15 SAR each")
        void testPopcornPrice() {
            BigDecimal subtotal = pricingService.calculatePopcornSubtotal(2);
            assertEquals(new BigDecimal("30.00"), subtotal);
        }
        
        @Test
        @DisplayName("Calculate total with popcorn")
        void testTotalWithPopcorn() {
            // 2 regular tickets (60) + 3 popcorn (45) = 105
            BigDecimal total = pricingService.calculateTotalPrice("regular", 2, 3);
            assertEquals(new BigDecimal("105.00"), total);
        }
        
        @Test
        @DisplayName("Bulk discount applied for 5+ tickets")
        void testBulkDiscount() {
            // 5 regular tickets = 150, with 10% discount = 135
            BigDecimal total = pricingService.calculateTotalPrice("regular", 5, 0);
            assertEquals(new BigDecimal("135.00"), total);
        }
        
        @Test
        @DisplayName("No bulk discount for less than 5 tickets")
        void testNoBulkDiscount() {
            // 4 regular tickets = 120 (no discount)
            BigDecimal total = pricingService.calculateTotalPrice("regular", 4, 0);
            assertEquals(new BigDecimal("120.00"), total);
            
            // Verify no discount calculated
            BigDecimal discount = pricingService.calculateDiscount("regular", 4, 0);
            assertEquals(BigDecimal.ZERO, discount);
        }
        
        @Test
        @DisplayName("Bulk discount includes popcorn in calculation")
        void testBulkDiscountWithPopcorn() {
            // 5 pro tickets (200) + 2 popcorn (30) = 230, with 10% discount = 207
            BigDecimal total = pricingService.calculateTotalPrice("pro", 5, 2);
            assertEquals(new BigDecimal("207.00"), total);
        }
        
        @Test
        @DisplayName("Price breakdown contains correct values")
        void testPriceBreakdown() {
            PricingService.PriceBreakdown breakdown = pricingService.getPriceBreakdown("pro", 6, 2);
            
            assertEquals(new BigDecimal("40.00"), breakdown.getTicketUnitPrice());
            assertEquals(6, breakdown.getTicketQuantity());
            assertEquals(new BigDecimal("240.00"), breakdown.getTicketSubtotal());
            assertEquals(new BigDecimal("15.00"), breakdown.getPopcornUnitPrice());
            assertEquals(2, breakdown.getPopcornQuantity());
            assertEquals(new BigDecimal("30.00"), breakdown.getPopcornSubtotal());
            
            // Total with discount: (240 + 30) * 0.9 = 243
            assertEquals(new BigDecimal("243.00"), breakdown.getTotal());
        }
        
        @Test
        @DisplayName("Zero tickets returns zero total")
        void testZeroTickets() {
            BigDecimal total = pricingService.calculateTotalPrice("regular", 0, 0);
            assertEquals(new BigDecimal("0.00"), total);
        }
    }
    
    /**
     * Tests for the DatabaseConnection Singleton pattern.
     */
    @Nested
    @DisplayName("DatabaseConnection Singleton Tests")
    class DatabaseConnectionSingletonTests {
        
        @Test
        @DisplayName("getInstance returns same instance")
        void testSingletonReturnsSameInstance() {
            DatabaseConnection instance1 = DatabaseConnection.getInstance();
            DatabaseConnection instance2 = DatabaseConnection.getInstance();
            
            assertSame(instance1, instance2, "getInstance should return the same instance");
        }
        
        @Test
        @DisplayName("getInstance is not null")
        void testSingletonNotNull() {
            DatabaseConnection instance = DatabaseConnection.getInstance();
            assertNotNull(instance, "getInstance should not return null");
        }
        
        @Test
        @DisplayName("Singleton is thread-safe")
        void testSingletonThreadSafety() throws InterruptedException {
            final DatabaseConnection[] instances = new DatabaseConnection[2];
            
            Thread t1 = new Thread(() -> instances[0] = DatabaseConnection.getInstance());
            Thread t2 = new Thread(() -> instances[1] = DatabaseConnection.getInstance());
            
            t1.start();
            t2.start();
            
            t1.join();
            t2.join();
            
            assertSame(instances[0], instances[1], "Concurrent calls should return the same instance");
        }
        
        @Test
        @DisplayName("Cannot clone singleton")
        void testCannotCloneSingleton() {
            DatabaseConnection instance = DatabaseConnection.getInstance();
            
            assertThrows(CloneNotSupportedException.class, () -> {
                // Using reflection to access the protected clone method
                java.lang.reflect.Method cloneMethod = Object.class.getDeclaredMethod("clone");
                cloneMethod.setAccessible(true);
                cloneMethod.invoke(instance);
            });
        }
    }
    
    /**
     * Tests for booking model.
     */
    @Nested
    @DisplayName("Booking Model Tests")
    class BookingModelTests {
        
        @Test
        @DisplayName("Booking constructor sets all fields")
        void testBookingConstructor() {
            sa.edu.kau.fcit.cpit252.project.model.Booking booking = 
                new sa.edu.kau.fcit.cpit252.project.model.Booking(
                    1, "test@example.com", "pro", 3, 2, new BigDecimal("150.00")
                );
            
            assertEquals(1, booking.getShowId());
            assertEquals("test@example.com", booking.getUserEmail());
            assertEquals("pro", booking.getTicketType());
            assertEquals(3, booking.getTicketQuantity());
            assertEquals(2, booking.getPopcornQuantity());
            assertEquals(new BigDecimal("150.00"), booking.getTotalPrice());
            assertNotNull(booking.getBookingDate());
        }
        
        @Test
        @DisplayName("Booking setters and getters work correctly")
        void testBookingSettersGetters() {
            sa.edu.kau.fcit.cpit252.project.model.Booking booking = 
                new sa.edu.kau.fcit.cpit252.project.model.Booking();
            
            booking.setBookingId(100);
            booking.setShowId(5);
            booking.setUserEmail("user@test.com");
            booking.setTicketType("regular");
            booking.setTicketQuantity(2);
            booking.setPopcornQuantity(1);
            booking.setTotalPrice(new BigDecimal("75.00"));
            
            assertEquals(100, booking.getBookingId());
            assertEquals(5, booking.getShowId());
            assertEquals("user@test.com", booking.getUserEmail());
            assertEquals("regular", booking.getTicketType());
            assertEquals(2, booking.getTicketQuantity());
            assertEquals(1, booking.getPopcornQuantity());
            assertEquals(new BigDecimal("75.00"), booking.getTotalPrice());
        }
    }
}
