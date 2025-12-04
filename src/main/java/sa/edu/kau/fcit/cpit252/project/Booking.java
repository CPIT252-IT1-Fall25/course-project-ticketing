package sa.edu.kau.fcit.cpit252.project;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}


