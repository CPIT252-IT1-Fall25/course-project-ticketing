[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Fv869B0L)
# 🎬 FAKEnetflix – Online Movie Ticketing System  
A CPIT-252 project implementing a modular, design-pattern–driven movie ticketing web application using Java Servlets, MVC, and in-memory storage.

---

## Overview  
FAKEnetflix is a simplified online movie ticketing platform where users can:

- Create an account  
- Log in using a Strategy-based authentication system  
- Browse available movies and showtimes  
- Check seat availability  
- Book tickets  
- View personal account information  

The project demonstrates clean coding principles, proper software layering, and multiple design patterns as required in the CPIT-252 course.

---

##  Project Architecture  
The system follows MVC with clear separation of concerns:
project/
│
├── model/ → User, Movie, Show, Booking
├── service/ → AuthService, MovieService, BookingService, ShowService
├── store/ → Singleton InMemoryUserStore, InMemoryBookingStore
├── controller/ → Servlets for Login, Signup, Booking, Seat Availability
└── strategy/ → LoginStrategy + PlainTextLoginStrategy

Each layer is responsible for a single role, improving maintainability and scalability.

---

##  Design Patterns Used  

###  **1. Singleton Pattern**  
Used for:
- `InMemoryUserStore`  
- `InMemoryBookingStore`  

Ensures a single shared instance of each store exists across the entire application.

---

###  **2. Strategy Pattern**  
Used for authentication:

`LoginStrategy` interface + `PlainTextLoginStrategy` implementation.

Allows the system to plug in new authentication methods without modifying core logic.

---

###  **3. Factory Method Pattern**  
Used to create Booking objects via a BookingFactory.  
Encapsulates object creation and improves extensibility.

---

##  Features  

###  **User Features**
- Account creation  
- Secure login  
- "Remember me" support  
- View profile information  

###  **Movie & Show Features**
- Browse movies  
- See showtimes  
- Check seat availability  

###  **Booking Features**
- Book a ticket  
- Store bookings per user  
- In-memory fast booking logic  

###  UI Features
- Styled login and signup pages  
- Netflix-inspired layout  
- Dark mode toggle  
- Responsive design  

---

##  How to Run the Project  

### **1. Clone the Repository**
```sh
git clone https://github.com/YOUR_USERNAME/project-ticketing.git
cd project-ticketing
