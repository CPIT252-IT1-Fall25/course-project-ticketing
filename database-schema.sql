-- Cinema Booking Database Schema for Azure SQL
-- Run this script to create the database tables

-- Movies table
CREATE TABLE Movies (
    movieId INT PRIMARY KEY IDENTITY(1,1),
    movieName NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    imageUrl NVARCHAR(500),
    createdAt DATETIME2 DEFAULT GETDATE()
);

-- Shows table (Movie + Location + Time combination)
CREATE TABLE Shows (
    showId INT PRIMARY KEY IDENTITY(1,1),
    movieId INT NOT NULL,
    location NVARCHAR(100) NOT NULL, -- 'Jeddah – Town Square Jeddah' or 'Riyadh – Al Yasmin district'
    showTime NVARCHAR(50) NOT NULL, -- '7:00 PM' or '10:30 PM'
    hallType NVARCHAR(50), -- 'Standard Hall' or 'Premium Hall'
    totalSeats INT NOT NULL DEFAULT 100,
    availableSeats INT NOT NULL DEFAULT 100,
    createdAt DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (movieId) REFERENCES Movies(movieId) ON DELETE CASCADE,
    UNIQUE (movieId, location, showTime) -- Ensure each movie+location+time is unique
);

-- Bookings table
CREATE TABLE Bookings (
    bookingId INT PRIMARY KEY IDENTITY(1,1),
    showId INT NOT NULL,
    userId INT, -- Reference to user (can be email or user ID)
    userEmail NVARCHAR(255),
    ticketType NVARCHAR(50) NOT NULL, -- 'Regular' or 'PRO'
    ticketQuantity INT NOT NULL,
    popcornQuantity INT DEFAULT 0,
    totalPrice DECIMAL(10,2) NOT NULL,
    bookingDate DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (showId) REFERENCES Shows(showId) ON DELETE CASCADE
);

-- Indexes for better performance
CREATE INDEX idx_shows_movie_location_time ON Shows(movieId, location, showTime);
CREATE INDEX idx_bookings_show ON Bookings(showId);
CREATE INDEX idx_bookings_user ON Bookings(userEmail);


