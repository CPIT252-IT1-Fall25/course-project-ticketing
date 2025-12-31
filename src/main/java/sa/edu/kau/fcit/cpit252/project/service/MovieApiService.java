package sa.edu.kau.fcit.cpit252.project.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Service to fetch movie information from external OMDB API.
 * This demonstrates integration with external REST APIs.
 */
public class MovieApiService {
    
    // OMDB API - Free tier allows 1000 requests/day
    private static final String OMDB_API_KEY = System.getenv("OMDB_API_KEY") != null 
        ? System.getenv("OMDB_API_KEY") 
        : "26f6a93c";
    
    private static final String OMDB_BASE_URL = "https://www.omdbapi.com/";
    
    private final OkHttpClient httpClient;
    private final Gson gson;
    
    public MovieApiService() {
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
        this.gson = new Gson();
    }
    
    /**
     * Search for a movie by title.
     * 
     * @param title Movie title to search
     * @return MovieInfo object with details, or null if not found
     */
    public MovieInfo searchMovie(String title) throws IOException {
        String url = OMDB_BASE_URL + "?apikey=" + OMDB_API_KEY + "&t=" + encodeUrl(title);
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            
            String responseBody = response.body().string();
            JsonObject json = gson.fromJson(responseBody, JsonObject.class);
            
            // Check if movie was found
            if (json.has("Response") && json.get("Response").getAsString().equals("False")) {
                return null;
            }
            
            return gson.fromJson(responseBody, MovieInfo.class);
        }
    }
    
    /**
     * Search for movies by keyword.
     * 
     * @param keyword Keyword to search
     * @return MovieSearchResult with list of movies
     */
    public MovieSearchResult searchMovies(String keyword) throws IOException {
        String url = OMDB_BASE_URL + "?apikey=" + OMDB_API_KEY + "&s=" + encodeUrl(keyword);
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            
            String responseBody = response.body().string();
            return gson.fromJson(responseBody, MovieSearchResult.class);
        }
    }
    
    /**
     * Get detailed movie information by IMDB ID.
     * 
     * @param imdbId IMDB ID (e.g., "tt0111161")
     * @return MovieInfo object with full details
     */
    public MovieInfo getMovieById(String imdbId) throws IOException {
        String url = OMDB_BASE_URL + "?apikey=" + OMDB_API_KEY + "&i=" + imdbId + "&plot=full";
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            
            String responseBody = response.body().string();
            JsonObject json = gson.fromJson(responseBody, JsonObject.class);
            
            if (json.has("Response") && json.get("Response").getAsString().equals("False")) {
                return null;
            }
            
            return gson.fromJson(responseBody, MovieInfo.class);
        }
    }
    
    private String encodeUrl(String value) {
        return value.replace(" ", "+").replace("&", "%26");
    }
    
    /**
     * Data class for movie information from OMDB API.
     */
    public static class MovieInfo {
        @SerializedName("Title")
        private String title;
        
        @SerializedName("Year")
        private String year;
        
        @SerializedName("Rated")
        private String rated;
        
        @SerializedName("Released")
        private String released;
        
        @SerializedName("Runtime")
        private String runtime;
        
        @SerializedName("Genre")
        private String genre;
        
        @SerializedName("Director")
        private String director;
        
        @SerializedName("Writer")
        private String writer;
        
        @SerializedName("Actors")
        private String actors;
        
        @SerializedName("Plot")
        private String plot;
        
        @SerializedName("Language")
        private String language;
        
        @SerializedName("Country")
        private String country;
        
        @SerializedName("Awards")
        private String awards;
        
        @SerializedName("Poster")
        private String poster;
        
        @SerializedName("imdbRating")
        private String imdbRating;
        
        @SerializedName("imdbVotes")
        private String imdbVotes;
        
        @SerializedName("imdbID")
        private String imdbId;
        
        @SerializedName("Type")
        private String type;
        
        @SerializedName("BoxOffice")
        private String boxOffice;
        
        // Getters
        public String getTitle() { return title; }
        public String getYear() { return year; }
        public String getRated() { return rated; }
        public String getReleased() { return released; }
        public String getRuntime() { return runtime; }
        public String getGenre() { return genre; }
        public String getDirector() { return director; }
        public String getWriter() { return writer; }
        public String getActors() { return actors; }
        public String getPlot() { return plot; }
        public String getLanguage() { return language; }
        public String getCountry() { return country; }
        public String getAwards() { return awards; }
        public String getPoster() { return poster; }
        public String getImdbRating() { return imdbRating; }
        public String getImdbVotes() { return imdbVotes; }
        public String getImdbId() { return imdbId; }
        public String getType() { return type; }
        public String getBoxOffice() { return boxOffice; }
    }
    
    /**
     * Data class for movie search results.
     */
    public static class MovieSearchResult {
        @SerializedName("Search")
        private MovieSearchItem[] search;
        
        @SerializedName("totalResults")
        private String totalResults;
        
        @SerializedName("Response")
        private String response;
        
        public MovieSearchItem[] getSearch() { return search; }
        public String getTotalResults() { return totalResults; }
        public String getResponse() { return response; }
        public boolean isSuccess() { return "True".equals(response); }
    }
    
    /**
     * Data class for individual search result items.
     */
    public static class MovieSearchItem {
        @SerializedName("Title")
        private String title;
        
        @SerializedName("Year")
        private String year;
        
        @SerializedName("imdbID")
        private String imdbId;
        
        @SerializedName("Type")
        private String type;
        
        @SerializedName("Poster")
        private String poster;
        
        public String getTitle() { return title; }
        public String getYear() { return year; }
        public String getImdbId() { return imdbId; }
        public String getType() { return type; }
        public String getPoster() { return poster; }
    }
}

