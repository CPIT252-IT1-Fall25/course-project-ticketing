package sa.edu.kau.fcit.cpit252.project;

public class Show {
    private int showId;
    private int movieId;
    private String location;
    private String showTime;
    private String hallType;
    private int totalSeats;
    private int availableSeats;

    public Show() {
    }

    public Show(int showId, int movieId, String location, String showTime, 
                String hallType, int totalSeats, int availableSeats) {
        this.showId = showId;
        this.movieId = movieId;
        this.location = location;
        this.showTime = showTime;
        this.hallType = hallType;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getHallType() {
        return hallType;
    }

    public void setHallType(String hallType) {
        this.hallType = hallType;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}


