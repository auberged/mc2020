package at.technikumwien.mc2020.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import at.technikumwien.mc2020.R;
import at.technikumwien.mc2020.ui.main.MainActivity;

public class MovieModel {
    public int id;
    public String type;
    public String title;
    public String description;
    public double vote_average;
    public String poster_url;
    public String releaseDate;
    public List<String> genres;

    public MovieModel(){

    }

    public MovieModel(int id, String type, String title, String description, double vote_average, String poster_url, String releaseDate) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.vote_average = vote_average;
        this.poster_url = "https://image.tmdb.org/t/p/w600_and_h900_bestv2" + poster_url;
        if (releaseDate.contains("-"))
            releaseDate = releaseDate.split("-")[0];
        this.releaseDate = releaseDate;
        this.genres = new ArrayList<String>();
    }

    public void addGenre(int genreId) {
        String genre = GetGenreStringFromId(genreId);
        if (genre != null)
            this.genres.add(genre);
    }

    private String GetGenreStringFromId(int genreId) {
        Context context = MainActivity.mContext;
        if (this.type.equals("series")) {
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_tv_genre_comedy_value)))
                return context.getResources().getString(R.string.pref_tv_genre_label_comedy);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_tv_genre_mystery_value)))
                return context.getResources().getString(R.string.pref_tv_genre_label_mystery);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_tv_genre_documentary_value)))
                return context.getResources().getString(R.string.pref_tv_genre_label_documentary);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_tv_genre_action_adventure_value)))
                return context.getResources().getString(R.string.pref_tv_genre_label_action_adventure);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_tv_genre_animation_value)))
                return context.getResources().getString(R.string.pref_tv_genre_label_animation);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_tv_genre_drama_value)))
                return context.getResources().getString(R.string.pref_tv_genre_label_drama);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_tv_genre_reality_value)))
                return context.getResources().getString(R.string.pref_tv_genre_label_reality);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_tv_genre_western_value)))
                return context.getResources().getString(R.string.pref_tv_genre_label_western);
            else
                return null;

        } else {
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_movie_genre_comedy_value)))
                return context.getResources().getString(R.string.pref_movie_genre_label_comedy);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_movie_genre_horror_value)))
                return context.getResources().getString(R.string.pref_movie_genre_label_horror);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_movie_genre_documentary_value)))
                return context.getResources().getString(R.string.pref_movie_genre_label_documentary);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_movie_genre_action_value)))
                return context.getResources().getString(R.string.pref_movie_genre_label_action);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_movie_genre_adventure_value)))
                return context.getResources().getString(R.string.pref_movie_genre_label_adventure);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_movie_genre_animation_value)))
                return context.getResources().getString(R.string.pref_movie_genre_label_animation);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_movie_genre_drama_value)))
                return context.getResources().getString(R.string.pref_movie_genre_label_drama);
            if (genreId == Integer.parseInt(context.getResources().getString(R.string.pref_movie_genre_western_value)))
                return context.getResources().getString(R.string.pref_movie_genre_label_western);
            else
                return null;
        }

    }
}
