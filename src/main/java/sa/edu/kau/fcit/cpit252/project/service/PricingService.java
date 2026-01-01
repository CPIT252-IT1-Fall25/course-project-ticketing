package sa.edu.kau.fcit.cpit252.project.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service class for handling all price calculations.
 * Centralizes pricing logic on the server-side for security and consistency.
 */
public class PricingService {
    
    // Pricing constants (could be loaded from database or config)
    public static final BigDecimal REGULAR_TICKET_PRICE = new BigDecimal("30.00");
    public static final BigDecimal PRO_TICKET_PRICE = new BigDecimal("40.00");
    public static final BigDecimal POPCORN_PRICE = new BigDecimal("15.00");
    
    // Discount thresholds
    public static final int BULK_DISCOUNT_THRESHOLD = 5;
    public static final BigDecimal BULK_DISCOUNT_PERCENTAGE = new BigDecimal("0.10"); // 10% off
    
    /**
     * Calculates the total price for a booking.
     * 
     * @param ticketType "regular" or "pro"
     * @param ticketQuantity number of tickets
     * @param popcornQuantity number of popcorn portions
     * @return calculated total price
     */
    public BigDecimal calculateTotalPrice(String ticketType, int ticketQuantity, int popcornQuantity) {
        BigDecimal ticketPrice = getTicketPrice(ticketType);
        BigDecimal ticketTotal = ticketPrice.multiply(new BigDecimal(ticketQuantity));
        BigDecimal popcornTotal = POPCORN_PRICE.multiply(new BigDecimal(popcornQuantity));
        
        BigDecimal subtotal = ticketTotal.add(popcornTotal);
        
        // Apply bulk discount if applicable
        if (ticketQuantity >= BULK_DISCOUNT_THRESHOLD) {
            BigDecimal discount = subtotal.multiply(BULK_DISCOUNT_PERCENTAGE);
            subtotal = subtotal.subtract(discount);
        }
        
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Gets the price for a ticket type.
     * 
     * @param ticketType "regular" or "pro"
     * @return price per ticket
     */
    public BigDecimal getTicketPrice(String ticketType) {
        if ("pro".equalsIgnoreCase(ticketType)) {
            return PRO_TICKET_PRICE;
        }
        return REGULAR_TICKET_PRICE;
    }
    
    /**
     * Calculates the ticket subtotal.
     */
    public BigDecimal calculateTicketSubtotal(String ticketType, int quantity) {
        return getTicketPrice(ticketType).multiply(new BigDecimal(quantity));
    }
    
    /**
     * Calculates the popcorn subtotal.
     */
    public BigDecimal calculatePopcornSubtotal(int quantity) {
        return POPCORN_PRICE.multiply(new BigDecimal(quantity));
    }
    
    /**
     * Calculates the discount amount if applicable.
     */
    public BigDecimal calculateDiscount(String ticketType, int ticketQuantity, int popcornQuantity) {
        if (ticketQuantity < BULK_DISCOUNT_THRESHOLD) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal subtotal = calculateTicketSubtotal(ticketType, ticketQuantity)
                .add(calculatePopcornSubtotal(popcornQuantity));
        
        return subtotal.multiply(BULK_DISCOUNT_PERCENTAGE).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Returns a detailed price breakdown.
     */
    public PriceBreakdown getPriceBreakdown(String ticketType, int ticketQuantity, int popcornQuantity) {
        BigDecimal ticketSubtotal = calculateTicketSubtotal(ticketType, ticketQuantity);
        BigDecimal popcornSubtotal = calculatePopcornSubtotal(popcornQuantity);
        BigDecimal discount = calculateDiscount(ticketType, ticketQuantity, popcornQuantity);
        BigDecimal total = calculateTotalPrice(ticketType, ticketQuantity, popcornQuantity);
        
        return new PriceBreakdown(
            getTicketPrice(ticketType),
            ticketQuantity,
            ticketSubtotal,
            POPCORN_PRICE,
            popcornQuantity,
            popcornSubtotal,
            discount,
            total
        );
    }
    
    /**
     * Inner class to represent a detailed price breakdown.
     */
    public static class PriceBreakdown {
        private BigDecimal ticketUnitPrice;
        private int ticketQuantity;
        private BigDecimal ticketSubtotal;
        private BigDecimal popcornUnitPrice;
        private int popcornQuantity;
        private BigDecimal popcornSubtotal;
        private BigDecimal discount;
        private BigDecimal total;
        
        public PriceBreakdown(BigDecimal ticketUnitPrice, int ticketQuantity, BigDecimal ticketSubtotal,
                              BigDecimal popcornUnitPrice, int popcornQuantity, BigDecimal popcornSubtotal,
                              BigDecimal discount, BigDecimal total) {
            this.ticketUnitPrice = ticketUnitPrice;
            this.ticketQuantity = ticketQuantity;
            this.ticketSubtotal = ticketSubtotal;
            this.popcornUnitPrice = popcornUnitPrice;
            this.popcornQuantity = popcornQuantity;
            this.popcornSubtotal = popcornSubtotal;
            this.discount = discount;
            this.total = total;
        }
        
        // Getters
        public BigDecimal getTicketUnitPrice() { return ticketUnitPrice; }
        public int getTicketQuantity() { return ticketQuantity; }
        public BigDecimal getTicketSubtotal() { return ticketSubtotal; }
        public BigDecimal getPopcornUnitPrice() { return popcornUnitPrice; }
        public int getPopcornQuantity() { return popcornQuantity; }
        public BigDecimal getPopcornSubtotal() { return popcornSubtotal; }
        public BigDecimal getDiscount() { return discount; }
        public BigDecimal getTotal() { return total; }
    }
}


