package at.technikumwien.mc2020.utilities;

import java.util.List;

public class MovieModel {
    public int id;
    public String title;
    public String description;
    public double vote_average;
    public String poster_url;
    public String releaseDate;
    public List<String> genres;

    public MovieModel(int id, String title, String description, double vote_average, String poster_url, String releaseDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.vote_average = vote_average;
        this.poster_url = "https://image.tmdb.org/t/p/w600_and_h900_bestv2" + poster_url;
        this.releaseDate = releaseDate;
    }

    public void addGenre(String genre) {
        this.genres.add(genre);
    }
}
