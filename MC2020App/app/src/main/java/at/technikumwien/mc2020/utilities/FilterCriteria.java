package at.technikumwien.mc2020.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

import at.technikumwien.mc2020.R;

public class FilterCriteria implements  SharedPreferences.OnSharedPreferenceChangeListener{
    private static FilterCriteria sInstance;

    private Context context;

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    private String type;

    private Set<String> genreList;

    private int releaseYear;

    private int imdbMinRating;

    private int imdbMaxRating;

    private FilterCriteria(Context context){
        this.context = context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setType(sharedPreferences);
        setGenreList(sharedPreferences);
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

    public Set<String> getGenreList() {
        return genreList;
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
    }

    private void setGenreList(@NotNull SharedPreferences pref){
        genreList = pref.getStringSet(context.getResources().getString(R.string.pref_tv_genre_key), null);
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
            setGenreList(sharedPreferences);
        } else if(key.equals(context.getResources().getString(R.string.pref_release_year_key))){
            setReleaseYear(sharedPreferences);
        } else if(key.equals(context.getResources().getString(R.string.pref_imdb_rating_key))){
            setImdbRating(sharedPreferences);
        }
    }
}
