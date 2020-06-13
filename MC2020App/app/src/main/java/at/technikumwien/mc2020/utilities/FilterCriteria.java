package at.technikumwien.mc2020.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import org.jetbrains.annotations.NotNull;

import at.technikumwien.mc2020.R;

public class FilterCriteria implements  SharedPreferences.OnSharedPreferenceChangeListener{
    private static FilterCriteria sInstance;

    private Context context;

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    private String type;

    private String genre;

    private int releaseYear;

    private int imdbMinRating;

    private int imdbMaxRating;

    private FilterCriteria(Context context){
        this.context = context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setType(sharedPreferences);
        setGenre(sharedPreferences);
        setReleaseYear(sharedPreferences);
        setImdbRating(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public static FilterCriteria getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new FilterCriteria(context);
            }
        }
        return sInstance;
    }

    public String getType() {
        return type;
    }

    public String getGenre() {
        return genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getImdbMinRating() {
        return imdbMinRating;
    }

    public int getImdbMaxRating() {
        return imdbMaxRating;
    }

    private void setType(@NotNull SharedPreferences pref){
        type = pref.getString(context.getResources().getString(R.string.pref_type_key), context.getResources().getString(R.string.pref_type_movies_value));

        setGenre(pref);
    }

    protected void setGenre(@NotNull SharedPreferences pref){
        String type = pref.getString(context.getResources().getString(R.string.pref_type_key), context.getResources().getString(R.string.pref_type_movies_value));

        if(type.equals(context.getResources().getString(R.string.pref_type_movies_value))){
            genre = pref.getString(context.getResources().getString(R.string.pref_movie_genre_key), context.getResources().getString(R.string.pref_movie_genre_default_value));
        }

        if(type.equals(context.getResources().getString(R.string.pref_type_series_value))){
            genre = pref.getString(context.getResources().getString(R.string.pref_tv_genre_key), context.getResources().getString(R.string.pref_tv_genre_default_value));
        }
}

    private void setReleaseYear(@NotNull SharedPreferences pref){
        releaseYear = pref.getInt(context.getResources().getString(R.string.pref_release_year_key), Integer.parseInt(context.getResources().getString(R.string.pref_release_year_default)));
    }

    private void setImdbRating(@NotNull SharedPreferences pref){
        String imdbRating = pref.getString(context.getResources().getString(R.string.pref_imdb_rating_key), context.getResources().getString(R.string.pref_imdb_rating_default));
        String[] values = imdbRating.split("-");
        imdbMinRating = Integer.valueOf(values[0]);
        imdbMaxRating = Integer.valueOf(values[1]);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(context.getResources().getString(R.string.pref_type_key))){
            setType(sharedPreferences);
        } else if(key.equals(context.getResources().getString(R.string.pref_tv_genre_key))){
            setGenre(sharedPreferences);
        } else if(key.equals(context.getResources().getString(R.string.pref_release_year_key))){
            setReleaseYear(sharedPreferences);
        } else if(key.equals(context.getResources().getString(R.string.pref_imdb_rating_key))){
            setImdbRating(sharedPreferences);
        }
    }
}
