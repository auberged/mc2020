package at.technikumwien.mc2020.utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
        this.genres = new ArrayList<String>();
    }

    public void addGenre(int genreId) {
        String genre = GetGenreStringFromId(genreId);
        this.genres.add(genre);
    }

    private String GetGenreStringFromId(int genreId) {
        if (genreId == 28)
            return "Action";
        if (genreId == 12)
            return "Abenteuer";
        if (genreId == 16)
            return "Animation";
        if (genreId == 35)
            return "Komödie";
        if (genreId == 80)
            return "Krimi";
        if (genreId == 99)
            return "Dokumentarfilm";
        if (genreId == 18)
            return "Drama";
        if (genreId == 10751)
            return "Familie";
        if (genreId == 14)
            return "Fantasy";
        if (genreId == 36)
            return "Historie";
        if (genreId == 27)
            return "Horror";
        if (genreId == 10402)
            return "Musik";
        if (genreId == 9648)
            return "Mystery";
        if (genreId == 10749)
            return "Liebesfilm";
        if (genreId == 878)
            return "Science Fiction";
        if (genreId == 10770)
            return "TV-Film";
        if (genreId == 53)
            return "Thriller";
        if (genreId == 10752)
            return "Kriegsfilm";
        if (genreId == 37)
            return "Western";
        else
            return "Unbekannt";
    }
}
