// Price constants
const PRICES = {
  regular: 30,
  pro: 40,
  popcorn: 15
};

// Get movie name from URL parameter or localStorage
function getMovieName() {
  const urlParams = new URLSearchParams(window.location.search);
  const movieName = urlParams.get('movie') || localStorage.getItem('selectedMovie');
  return movieName || 'Unknown Movie';
}

// Store movie name
const currentMovieName = getMovieName();
if (currentMovieName) {
  localStorage.setItem('selectedMovie', currentMovieName);
}

// Show seat availability
let currentShowId = null;
let availableSeats = 0;

// Get form elements
const form = document.getElementById('bookingForm');
const cinemaLocationInputs = document.querySelectorAll('input[name="cinemaLocation"]');
const hallTypeSection = document.getElementById('hallTypeSection');
const showTimeSection = document.getElementById('showTimeSection');
const ticketTypeSection = document.getElementById('ticketTypeSection');
const ticketQuantitySection = document.getElementById('ticketQuantitySection');
const popcornSection = document.getElementById('popcornSection');
const popcornCheckbox = document.getElementById('popcornCheckbox');
const popcornQuantityDiv = document.getElementById('popcornQuantityDiv');
const popcornQuantityInput = document.getElementById('popcornQuantity');
const priceSummary = document.getElementById('priceSummary');
const submitBtn = document.getElementById('submitBtn');

// Show sections progressively
cinemaLocationInputs.forEach(input => {
  input.addEventListener('change', function() {
    if (this.checked) {
      // Show hall type section after cinema location is selected
      hallTypeSection.style.display = 'block';
      // Reset seat availability when location changes
      currentShowId = null;
      availableSeats = 0;
    }
  });
});

// Show show time section after hall type is selected
const hallTypeSelect = document.getElementById('hallType');
hallTypeSelect.addEventListener('change', function() {
  if (this.value) {
    showTimeSection.style.display = 'block';
    // Reset seat availability when hall type changes
    currentShowId = null;
    availableSeats = 0;
  } else {
    showTimeSection.style.display = 'none';
    ticketTypeSection.style.display = 'none';
    ticketQuantitySection.style.display = 'none';
    popcornSection.style.display = 'none';
    priceSummary.style.display = 'none';
    submitBtn.style.display = 'none';
  }
});

// Show ticket type section after show time is selected
const showTimeInputs = document.querySelectorAll('input[name="showTime"]');
showTimeInputs.forEach(input => {
  input.addEventListener('change', function() {
    if (this.checked) {
      checkSeatAvailability();
    }
  });
});

// Show quantity and popcorn sections after ticket type is selected
const ticketTypeInputs = document.querySelectorAll('input[name="ticketType"]');
ticketTypeInputs.forEach(input => {
  input.addEventListener('change', function() {
    if (this.checked) {
      ticketQuantitySection.style.display = 'block';
      popcornSection.style.display = 'block';
      priceSummary.style.display = 'block';
      submitBtn.style.display = 'block';
      updateSeatAvailabilityDisplay();
      calculatePrice();
    }
  });
});

// Handle popcorn checkbox
popcornCheckbox.addEventListener('change', function() {
  if (this.checked) {
    popcornQuantityDiv.style.display = 'block';
  } else {
    popcornQuantityDiv.style.display = 'none';
    popcornQuantityInput.value = 1;
  }
  calculatePrice();
});

// Calculate and update price
function calculatePrice() {
  const ticketTypeInput = document.querySelector('input[name="ticketType"]:checked');
  const ticketQuantity = parseInt(document.getElementById('ticketQuantity').value) || 0;
  const popcornQuantity = popcornCheckbox.checked 
    ? (parseInt(popcornQuantityInput.value) || 0) 
    : 0;

  if (!ticketTypeInput || ticketQuantity === 0) {
    return;
  }

  const ticketType = ticketTypeInput.value;
  const ticketPricePerUnit = PRICES[ticketType];
  const totalTicketPrice = ticketPricePerUnit * ticketQuantity;
  const totalPopcornPrice = PRICES.popcorn * popcornQuantity;
  const totalPrice = totalTicketPrice + totalPopcornPrice;

  // Update price display
  document.getElementById('ticketPrice').textContent = `${totalTicketPrice} SAR`;
  
  if (popcornQuantity > 0) {
    document.getElementById('popcornPriceItem').style.display = 'flex';
    document.getElementById('popcornPrice').textContent = `${totalPopcornPrice} SAR`;
  } else {
    document.getElementById('popcornPriceItem').style.display = 'none';
  }
  
  document.getElementById('totalPrice').textContent = `${totalPrice} SAR`;
}

// Check seat availability
async function checkSeatAvailability() {
  const cinemaLocation = document.querySelector('input[name="cinemaLocation"]:checked');
  const hallType = document.getElementById('hallType').value;
  const showTime = document.querySelector('input[name="showTime"]:checked');
  
  if (!cinemaLocation || !showTime) {
    return;
  }
  
  const location = cinemaLocation.value === 'jeddah' 
    ? 'Jeddah – Town Square Jeddah' 
    : 'Riyadh – Al Yasmin district';
  
  const params = new URLSearchParams({
    movieName: currentMovieName,
    location: location,
    showTime: showTime.value,
    hallType: hallType || 'Standard Hall'
  });
  
  try {
    const response = await fetch(`/seatavailability?${params}`);
    const data = await response.json();
    
    if (data.error) {
      console.error('Error checking seats:', data.error);
      availableSeats = 0;
      currentShowId = null;
    } else {
      currentShowId = data.showId;
      availableSeats = data.availableSeats;
      updateSeatAvailabilityDisplay();
    }
  } catch (error) {
    console.error('Error checking seat availability:', error);
    availableSeats = 0;
    currentShowId = null;
  }
}

// Update seat availability display
function updateSeatAvailabilityDisplay() {
  // Remove existing seat info if any
  let seatInfo = document.getElementById('seatAvailabilityInfo');
  if (!seatInfo) {
    seatInfo = document.createElement('div');
    seatInfo.id = 'seatAvailabilityInfo';
    seatInfo.className = 'seat-availability-info';
    const ticketQuantitySection = document.getElementById('ticketQuantitySection');
    if (ticketQuantitySection) {
      ticketQuantitySection.parentNode.insertBefore(seatInfo, ticketQuantitySection);
    }
  }
  
  if (availableSeats > 0) {
    seatInfo.innerHTML = `<p style="color: var(--text-secondary); margin: 10px 0;">Available Seats: <strong style="color: var(--accent-color);">${availableSeats}</strong></p>`;
    seatInfo.style.display = 'block';
    
    // Set max tickets to available seats
    const ticketQuantityInput = document.getElementById('ticketQuantity');
    if (ticketQuantityInput) {
      ticketQuantityInput.max = availableSeats;
      if (parseInt(ticketQuantityInput.value) > availableSeats) {
        ticketQuantityInput.value = availableSeats;
        calculatePrice();
      }
    }
  } else {
    seatInfo.innerHTML = `<p style="color: #e50914; margin: 10px 0;">No seats available for this show</p>`;
    seatInfo.style.display = 'block';
    submitBtn.style.display = 'none';
  }
}

// Add event listeners for quantity changes
document.getElementById('ticketQuantity').addEventListener('input', function() {
  const maxSeats = availableSeats;
  if (parseInt(this.value) > maxSeats) {
    this.value = maxSeats;
    alert(`Only ${maxSeats} seats available`);
  }
  calculatePrice();
});
popcornQuantityInput.addEventListener('input', calculatePrice);

// Form submission
form.addEventListener('submit', async function(e) {
  e.preventDefault();
  
  // Validate form
  const cinemaLocation = document.querySelector('input[name="cinemaLocation"]:checked');
  const hallType = document.getElementById('hallType').value;
  const showTime = document.querySelector('input[name="showTime"]:checked');
  const ticketType = document.querySelector('input[name="ticketType"]:checked');
  const ticketQuantity = parseInt(document.getElementById('ticketQuantity').value);

  if (!cinemaLocation || !hallType || !showTime || !ticketType || !ticketQuantity || ticketQuantity < 1) {
    alert('Please fill in all required fields correctly.');
    return;
  }

  // Check seat availability one more time
  if (ticketQuantity > availableSeats) {
    alert(`Not enough seats available. Only ${availableSeats} seats remaining.`);
    await checkSeatAvailability();
    return;
  }

  const location = cinemaLocation.value === 'jeddah' 
    ? 'Jeddah – Town Square Jeddah' 
    : 'Riyadh – Al Yasmin district';

  // Prepare booking data
  const bookingData = {
    movieName: currentMovieName,
    location: location,
    showTime: showTime.value,
    hallType: hallTypeSelect.options[hallTypeSelect.selectedIndex].text,
    ticketType: ticketType.value === 'regular' ? 'Regular' : 'PRO',
    ticketQuantity: ticketQuantity,
    popcornQuantity: popcornCheckbox.checked ? parseInt(popcornQuantityInput.value) : 0,
    totalPrice: calculateTotalPrice()
  };

  // Send booking request to server
  try {
    const formData = new URLSearchParams();
    formData.append('movieName', bookingData.movieName);
    formData.append('location', bookingData.location);
    formData.append('showTime', bookingData.showTime);
    formData.append('hallType', bookingData.hallType);
    formData.append('ticketType', bookingData.ticketType);
    formData.append('ticketQuantity', bookingData.ticketQuantity.toString());
    formData.append('popcornQuantity', bookingData.popcornQuantity.toString());
    formData.append('totalPrice', bookingData.totalPrice.toString());

    const response = await fetch('/booking', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: formData.toString()
    });

    const result = await response.json();

    if (result.success) {
      // Display success message
      const summary = `
        Booking Successful!
        Booking ID: ${result.bookingId}
        
        Booking Summary:
        - Movie: ${bookingData.movieName}
        - Cinema: ${bookingData.location}
        - Hall Type: ${bookingData.hallType}
        - Show Time: ${bookingData.showTime}
        - Ticket Type: ${bookingData.ticketType}
        - Number of Tickets: ${bookingData.ticketQuantity}
        ${bookingData.popcornQuantity > 0 ? `- Popcorn: ${bookingData.popcornQuantity} portions` : ''}
        - Total Price: ${bookingData.totalPrice} SAR
      `;

      alert(summary);
      
      // Refresh seat availability
      await checkSeatAvailability();
      
      // Optionally redirect or reset form
      // window.location.href = 'index.html';
    } else {
      alert(`Booking failed: ${result.error}`);
      // Refresh seat availability in case seats were taken
      await checkSeatAvailability();
    }
  } catch (error) {
    console.error('Error creating booking:', error);
    alert('An error occurred while processing your booking. Please try again.');
  }
});

// Helper function to calculate total price
function calculateTotalPrice() {
  const ticketTypeInput = document.querySelector('input[name="ticketType"]:checked');
  const ticketQuantity = parseInt(document.getElementById('ticketQuantity').value) || 0;
  const popcornQuantity = popcornCheckbox.checked 
    ? (parseInt(popcornQuantityInput.value) || 0) 
    : 0;

  if (!ticketTypeInput) return 0;

  const ticketType = ticketTypeInput.value;
  const ticketPricePerUnit = PRICES[ticketType];
  const totalTicketPrice = ticketPricePerUnit * ticketQuantity;
  const totalPopcornPrice = PRICES.popcorn * popcornQuantity;

  return totalTicketPrice + totalPopcornPrice;
}
