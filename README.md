[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Fv869B0L)
# 🎬 FAKEnetflix – Online Movie Ticketing System  
A CPIT-252 project implementing a modular, design-pattern–driven movie ticketing web application using Java Servlets, MVC, and Azure SQL Database.

---

## Overview  
FAKEnetflix is a simplified online movie ticketing platform where users can:

- Create an account  
- Log in using a Strategy-based authentication system  
- Search movies using OMDB external API  
- Browse available movies and showtimes  
- Check seat availability  
- Book tickets with server-side price calculation  
- View personal account information  

The project demonstrates clean coding principles, proper software layering, and multiple design patterns as required in the CPIT-252 course.

---

## Project Architecture  
The system follows MVC with clear separation of concerns:

```
project/
│
├── model/       → User, Movie, Show, Booking
├── service/     → AuthService, PricingService, MovieApiService, BookingService, ShowService
├── store/       → DatabaseConnection (Singleton), InMemoryUserStore
├── controller/  → Servlets for Login, Signup, Booking, Price Calculation, Movie API
└── strategy/    → LoginStrategy + PlainTextLoginStrategy + HashedLoginStrategy
```

Each layer is responsible for a single role, improving maintainability and scalability.

---

## Design Patterns Used  

### 1. Singleton Pattern  
Used for `DatabaseConnection` to ensure a single database connection manager exists across the application. Implements thread-safe double-checked locking with volatile instance.

### 2. Strategy Pattern  
Used for authentication with `LoginStrategy` interface and multiple implementations (`PlainTextLoginStrategy`, `HashedLoginStrategy`). Allows the system to plug in new authentication methods without modifying core logic.

### 3. Builder Pattern  
Used in `Booking` class for creating complex booking objects with a fluent API. Includes validation and default values.

---

## Features  

### User Features
- Account creation  
- Secure login with pluggable authentication  
- View profile information  

### Movie & Show Features
- Search movies from OMDB external API  
- Browse movies  
- See showtimes  
- Check seat availability  

### Booking Features
- Book tickets  
- Server-side price calculation  
- Bulk discount for 5+ tickets  
- Popcorn add-on option  

### UI Features
- Netflix-inspired layout  
- Dark mode toggle  
- Movie search bar with results modal  
- Responsive design  

---

## Technologies Used

- Java 17
- Jakarta Servlets
- Jetty (Embedded Server)
- Maven
- Azure SQL Database
- Gson (JSON)
- OkHttp (HTTP Client)
- OMDB API (External Movie Database)
- JUnit 5 (Testing)
- Mockito (Mocking)

---

## How to Run the Project  

### 1. Clone the Repository
```sh
git clone https://github.com/YOUR_USERNAME/course-project-ticketing.git
cd course-project-ticketing
```

### 2. Build the Project
```sh
mvn clean package
```

### 3. Run the Server
```sh
mvn exec:java
```
Or run the JAR:
```sh
java -jar target/course-project-1.0-SNAPSHOT-shaded.jar
```

### 4. Open in Browser
Open `index.html` in your browser. The API runs on `http://localhost:8080`.

---

## Testing

Unit tests are implemented using JUnit 5 and Mockito covering:
- Pricing calculations
- Singleton pattern verification
- Strategy pattern authentication
- Builder pattern validation
- Model classes

Run tests with:
```sh
git clone https://github.com/YOUR_USERNAME/project-ticketing.git
cd project-ticketing
