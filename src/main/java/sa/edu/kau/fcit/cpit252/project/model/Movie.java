package sa.edu.kau.fcit.cpit252.project.model;


public class Movie {
    private int movieId;
    private String movieName;
    private String description;
    private String imageUrl;

    public Movie() {
    }

    public Movie(int movieId, String movieName, String description, String imageUrl) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


