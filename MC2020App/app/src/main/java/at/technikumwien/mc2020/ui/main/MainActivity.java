package at.technikumwien.mc2020.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.technikumwien.mc2020.MovieCard;
import at.technikumwien.mc2020.R;
import at.technikumwien.mc2020.ui.settings.SettingsActivity;
import at.technikumwien.mc2020.utilities.MovieModel;
import at.technikumwien.mc2020.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    private static final int LOADER_ID = 4012;
    private static final String DATA_EXTRA = "data";
    private static final String PAGE_NR_EXTRA = "page_nr";
    private static final String API_URL_EXTRA = "api_url";

    private static int PAGE_NUMBER = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipe_view);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(-50)
                        .setRelativeScale(0.05f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        // Instead of Image url, we want to load the Movie Class into the Movie Card
        mSwipeView.addView(new MovieCard("https://i.pinimg.com/originals/fd/5e/66/fd5e662dce1a3a8cd192a5952fa64f02.jpg", mContext, mSwipeView));
        loadData();

        /*imageButton = findViewById(R.id.id_preference_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });*/

    }


    private void loadData() {
        URL apiUrl = NetworkUtils.buildUrl("https://api.themoviedb.org/3/discover/movie?api_key=db94b1f559af23f5a8bd53a8dbec0c1e&language=de&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&year=2019", 5);

        Log.d("URL", apiUrl.toString());

        Bundle queryBundle = new Bundle();
        queryBundle.putString(API_URL_EXTRA, apiUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> apiLoader = loaderManager.getLoader(LOADER_ID);
        if(apiLoader == null){
            loaderManager.initLoader(LOADER_ID, queryBundle, this);
        } else{
            loaderManager.restartLoader(LOADER_ID, queryBundle, this);
        }

        PAGE_NUMBER++;

    }


    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String data;

            @Override
            public void onStartLoading() {

                if (args == null) {
                    return;
                }

                if (data != null) {
                    deliverResult(data);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String apiUrlString = args.getString(API_URL_EXTRA);

                // if url error
                if (apiUrlString.isEmpty()) return null;

                try {
                    URL apiUrl = new URL(apiUrlString);
                    String apiResult = NetworkUtils.getResponseFromHttpUrl(apiUrl);
                    return apiResult;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String result) {
                data = result;
                super.deliverResult(result);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data == null){
            showErrorToast();
        } else{
            try {
                List<MovieModel> movies = parseDataToMovies(data);
                for (MovieModel movie : movies ) {
                    mSwipeView.addView(new MovieCard(movie.poster_url, mContext, mSwipeView));
                    Log.d("URL", movie.id + ": " + movie.title + " - " + movie.poster_url);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public List<MovieModel> parseDataToMovies(String apiResult) throws JSONException {
        if(apiResult == null)
            return null;

        // Parse to object
        JSONObject jsonObject = new JSONObject(apiResult);

        System.out.println(jsonObject.toString());

        // Parse to array of cards
        JSONArray moviesArray = jsonObject.getJSONArray("results");

        // If page is empty, send new request to first page
        if(moviesArray.length() == 0){
            return null;
        }

        // Make a new empty list
        List<MovieModel> movies = new LinkedList<>();

        // Generate each card and add it to the list
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject jsonCard = moviesArray.getJSONObject(i);
            int id = jsonCard.getInt("id");
            String title = jsonCard.getString("title");
            String description = jsonCard.getString("overview");
            double vote_average = jsonCard.getDouble("vote_average");
            String poster_url = jsonCard.getString("poster_path");
            String releaseDate = jsonCard.getString("release_date");

            MovieModel movie = new MovieModel(id, title, description, vote_average, poster_url, releaseDate);

            JSONArray genres = jsonCard.getJSONArray("genre_ids");
            for (int j = 0; j < genres.length(); j++) {
                movie.addGenre(genres.getInt(j));
            }

            movies.add(movie);
        }

        return movies;

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        /*
         * We aren't using this method in our tinder app, but we are required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
    }

    private void showErrorToast() {
        Context context = getApplicationContext();
        CharSequence text = "ERRR";//getString(R.string.error_message);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt(PAGE_NR_EXTRA, PAGE_NUMBER);
//    }



    private void openSettingsActivity(){
        Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(startSettingsActivity);
    }
}
