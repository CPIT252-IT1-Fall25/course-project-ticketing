package sa.edu.kau.fcit.cpit252.project.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Booking model with Builder Pattern implementation.
 * Represents a movie ticket booking with pricing and user details.
 */
public class Booking {

    // ==================== Fields ====================
    private int bookingId;
    private int showId;
    private String userEmail;
    private String ticketType;
    private int ticketQuantity;
    private int popcornQuantity;
    private BigDecimal totalPrice;
    private LocalDateTime bookingDate;

    // ==================== Constructors ====================

    /** Default constructor */
    public Booking() {
    }

    /** Full constructor */
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

    /** Private constructor for Builder pattern */
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

    // ==================== Builder Factory ====================

    public static Builder builder() {
        return new Builder();
    }

    // ==================== Getters ====================

    public int getBookingId() {
        return bookingId;
    }

    public int getShowId() {
        return showId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getTicketType() {
        return ticketType;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public int getPopcornQuantity() {
        return popcornQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    // ==================== Setters ====================

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public void setPopcornQuantity(int popcornQuantity) {
        this.popcornQuantity = popcornQuantity;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    // ==================== Object Methods ====================

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

    // ==================== Builder Pattern ====================

    /**
     * Builder class for constructing Booking objects step-by-step.
     * Provides a fluent API for creating bookings.
     */
    public static class Builder {

        private int bookingId;
        private int showId;
        private String userEmail;
        private String ticketType = "regular";
        private int ticketQuantity = 1;
        private int popcornQuantity = 0;
        private BigDecimal totalPrice;
        private LocalDateTime bookingDate;

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
         * @return the constructed Booking
         * @throws IllegalStateException if required fields are missing or invalid
         */
        public Booking build() {
            if (userEmail == null || userEmail.trim().isEmpty()) {
                throw new IllegalStateException("User email is required");
            }
            if (ticketQuantity < 0) {
                throw new IllegalStateException("Ticket quantity cannot be negative");
            }
            if (popcornQuantity < 0) {
                throw new IllegalStateException("Popcorn quantity cannot be negative");
            }
            return new Booking(this);
        }
    }
}
