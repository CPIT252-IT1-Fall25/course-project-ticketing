package sa.edu.kau.fcit.cpit252.project;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sa.edu.kau.fcit.cpit252.project.model.Booking;

/**
 * Tests for the Builder Pattern implementation in Booking class.
 * Verifies that the Builder creates objects correctly and validates input.
 */
@DisplayName("Builder Pattern Tests")
public class BuilderPatternTest {
    
    @Test
    @DisplayName("Builder creates Booking with all fields")
    void testBuilderCreatesCompleteBooking() {
        LocalDateTime now = LocalDateTime.now();
        
        Booking booking = Booking.builder()
            .bookingId(1)
            .showId(10)
            .userEmail("user@example.com")
            .ticketType("pro")
            .ticketQuantity(3)
            .popcornQuantity(2)
            .totalPrice(new BigDecimal("150.00"))
            .bookingDate(now)
            .build();
        
        assertEquals(1, booking.getBookingId());
        assertEquals(10, booking.getShowId());
        assertEquals("user@example.com", booking.getUserEmail());
        assertEquals("pro", booking.getTicketType());
        assertEquals(3, booking.getTicketQuantity());
        assertEquals(2, booking.getPopcornQuantity());
        assertEquals(new BigDecimal("150.00"), booking.getTotalPrice());
        assertEquals(now, booking.getBookingDate());
    }
    
    @Test
    @DisplayName("Builder uses default values")
    void testBuilderDefaultValues() {
        Booking booking = Booking.builder()
            .userEmail("user@example.com")
            .build();
        
        assertEquals("regular", booking.getTicketType());
        assertEquals(1, booking.getTicketQuantity());
        assertEquals(0, booking.getPopcornQuantity());
        assertNotNull(booking.getBookingDate());
    }
    
    @Test
    @DisplayName("Builder validates required email")
    void testBuilderValidatesEmail() {
        assertThrows(IllegalStateException.class, () -> {
            Booking.builder()
                .ticketQuantity(2)
                .build();
        });
    }
    
    @Test
    @DisplayName("Builder validates email not empty")
    void testBuilderValidatesEmailNotEmpty() {
        assertThrows(IllegalStateException.class, () -> {
            Booking.builder()
                .userEmail("   ")
                .build();
        });
    }
    
    @Test
    @DisplayName("Builder validates ticket quantity not negative")
    void testBuilderValidatesTicketQuantity() {
        assertThrows(IllegalStateException.class, () -> {
            Booking.builder()
                .userEmail("user@example.com")
                .ticketQuantity(-1)
                .build();
        });
    }
    
    @Test
    @DisplayName("Builder validates popcorn quantity not negative")
    void testBuilderValidatesPopcornQuantity() {
        assertThrows(IllegalStateException.class, () -> {
            Booking.builder()
                .userEmail("user@example.com")
                .popcornQuantity(-1)
                .build();
        });
    }
    
    @Test
    @DisplayName("Builder allows zero quantities")
    void testBuilderAllowsZeroQuantities() {
        Booking booking = Booking.builder()
            .userEmail("user@example.com")
            .ticketQuantity(0)
            .popcornQuantity(0)
            .build();
        
        assertEquals(0, booking.getTicketQuantity());
        assertEquals(0, booking.getPopcornQuantity());
    }
    
    @Test
    @DisplayName("Builder provides fluent API")
    void testBuilderFluentApi() {
        // This test verifies that builder methods return the builder (fluent API)
        Booking.Builder builder = Booking.builder();
        
        assertSame(builder, builder.bookingId(1));
        assertSame(builder, builder.showId(1));
        assertSame(builder, builder.userEmail("test@test.com"));
        assertSame(builder, builder.ticketType("pro"));
        assertSame(builder, builder.ticketQuantity(1));
        assertSame(builder, builder.popcornQuantity(1));
        assertSame(builder, builder.totalPrice(BigDecimal.TEN));
        assertSame(builder, builder.bookingDate(LocalDateTime.now()));
    }
    
    @Test
    @DisplayName("Multiple bookings from same builder pattern")
    void testMultipleBookingsFromBuilder() {
        Booking booking1 = Booking.builder()
            .userEmail("user1@example.com")
            .ticketQuantity(2)
            .build();
        
        Booking booking2 = Booking.builder()
            .userEmail("user2@example.com")
            .ticketQuantity(4)
            .build();
        
        assertNotEquals(booking1.getUserEmail(), booking2.getUserEmail());
        assertNotEquals(booking1.getTicketQuantity(), booking2.getTicketQuantity());
    }
    
    @Test
    @DisplayName("Booking toString returns meaningful string")
    void testBookingToString() {
        Booking booking = Booking.builder()
            .bookingId(123)
            .userEmail("user@example.com")
            .ticketType("pro")
            .build();
        
        String str = booking.toString();
        assertTrue(str.contains("123"));
        assertTrue(str.contains("user@example.com"));
        assertTrue(str.contains("pro"));
    }
}


