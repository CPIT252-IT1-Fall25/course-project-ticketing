package sa.edu.kau.fcit.cpit252.project.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Booking model class with Builder Pattern implementation.
 * 
 * Design Pattern: Builder
 * - Separates construction of complex object from its representation
 * - Allows step-by-step construction
 * - Provides fluent API for creating bookings
 */
public class Booking {
    private int bookingId;
    private int showId;
    private String userEmail;
    private String ticketType;
    private int ticketQuantity;
    private int popcornQuantity;
    private BigDecimal totalPrice;
    private LocalDateTime bookingDate;

    public Booking() {
    }

    public Booking(int showId, String userEmail, String ticketType, 
                   int ticketQuantity, int popcornQuantity, BigDecimal totalPrice) {
        this.showId = showId;
        this.userEmail = userEmail;
        this.ticketType = ticketType;
        this.ticketQuantity = ticketQuantity;
        this.popcornQuantity = popcornQuantity;
        this.totalPrice = totalPrice;
        this.bookingDate = LocalDateTime.now();
    }
    
    // Private constructor for Builder
    private Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.showId = builder.showId;
        this.userEmail = builder.userEmail;
        this.ticketType = builder.ticketType;
        this.ticketQuantity = builder.ticketQuantity;
        this.popcornQuantity = builder.popcornQuantity;
        this.totalPrice = builder.totalPrice;
        this.bookingDate = builder.bookingDate != null ? builder.bookingDate : LocalDateTime.now();
    }
    
    /**
     * Creates a new Builder instance.
     * 
     * @return A new Builder for constructing Booking objects
     */
    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public int getPopcornQuantity() {
        return popcornQuantity;
    }

    public void setPopcornQuantity(int popcornQuantity) {
        this.popcornQuantity = popcornQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", showId=" + showId +
                ", userEmail='" + userEmail + '\'' +
                ", ticketType='" + ticketType + '\'' +
                ", ticketQuantity=" + ticketQuantity +
                ", popcornQuantity=" + popcornQuantity +
                ", totalPrice=" + totalPrice +
                ", bookingDate=" + bookingDate +
                '}';
    }
    
    /**
     * Builder Pattern - Inner Builder class for constructing Booking objects.
     * 
     * Usage example:
     * <pre>
     * Booking booking = Booking.builder()
     *     .showId(1)
     *     .userEmail("user@example.com")
     *     .ticketType("pro")
     *     .ticketQuantity(2)
     *     .popcornQuantity(1)
     *     .totalPrice(new BigDecimal("95.00"))
     *     .build();
     * </pre>
     */
    public static class Builder {
        private int bookingId;
        private int showId;
        private String userEmail;
        private String ticketType;
        private int ticketQuantity;
        private int popcornQuantity;
        private BigDecimal totalPrice;
        private LocalDateTime bookingDate;
        
        public Builder() {
            // Default values
            this.ticketQuantity = 1;
            this.popcornQuantity = 0;
            this.ticketType = "regular";
        }
        
        public Builder bookingId(int bookingId) {
            this.bookingId = bookingId;
            return this;
        }
        
        public Builder showId(int showId) {
            this.showId = showId;
            return this;
        }
        
        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }
        
        public Builder ticketType(String ticketType) {
            this.ticketType = ticketType;
            return this;
        }
        
        public Builder ticketQuantity(int ticketQuantity) {
            this.ticketQuantity = ticketQuantity;
            return this;
        }
        
        public Builder popcornQuantity(int popcornQuantity) {
            this.popcornQuantity = popcornQuantity;
            return this;
        }
        
        public Builder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }
        
        public Builder bookingDate(LocalDateTime bookingDate) {
            this.bookingDate = bookingDate;
            return this;
        }
        
        /**
         * Builds the Booking object.
         * 
         * @return A new Booking instance
         * @throws IllegalStateException if required fields are missing
         */
        public Booking build() {
            validate();
            return new Booking(this);
        }
        
        private void validate() {
            if (userEmail == null || userEmail.trim().isEmpty()) {
                throw new IllegalStateException("User email is required");
            }
            if (ticketQuantity < 0) {
                throw new IllegalStateException("Ticket quantity cannot be negative");
            }
            if (popcornQuantity < 0) {
                throw new IllegalStateException("Popcorn quantity cannot be negative");
            }
        }
    }
}


