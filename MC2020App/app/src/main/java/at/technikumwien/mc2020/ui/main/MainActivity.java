package at.technikumwien.mc2020.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.technikumwien.mc2020.MovieCard;
import at.technikumwien.mc2020.R;
import at.technikumwien.mc2020.data.database.FirebaseHandler;
import at.technikumwien.mc2020.ui.detail.DetailActivity;
import at.technikumwien.mc2020.ui.launcher.LauncherActivity;
import at.technikumwien.mc2020.ui.list.ListActivity;
import at.technikumwien.mc2020.ui.settings.SettingsActivity;
import at.technikumwien.mc2020.utilities.FilterCriteria;
import at.technikumwien.mc2020.utilities.MovieModel;
import at.technikumwien.mc2020.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    public static Context mContext;

    private SwipePlaceHolderView mSwipeView;
    private Intent startListActivity;
    private Intent startSettingsActivity;
    private Intent startDetailActivity;
    private List<MovieModel> movies = new ArrayList<>();
    private List<Integer> movies_ids = new ArrayList<>();

    private static final int LOADER_ID = 4012;
    private static final String DATA_EXTRA = "data";
    private static final String PAGE_NR_EXTRA = "page_nr";
    private static final String API_URL_EXTRA = "api_url";

    private static int PAGE_NUMBER = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FirebaseHandler.getInstance();

        setContentView(R.layout.activity_main);

        // simple connectivity check
        if (!isNetworkAvailable()) {
            Log.d("TINDER", "not connected");
            showErrorToast(getString(R.string.error_no_db_connection));
        }

        // if user isn't logged in, log in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivity(intent);
        }

        // set swiping Cards
        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipe_view);
        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {

            // if a card is removed, trigger
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemRemoved(int count) {
                // load new data if to less cards
                if ( (count <= 30) && (count % 5 == 0) || (count <= 50) && (count % 10 == 0)) {
                    Log.d("TINDER", "load more .....");
                    loadData();
                }
            }
        });
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(-50)
                        .setRelativeScale(0.05f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


        // init the three button (dislike, more info and like)
        View dislikeButton = findViewById(R.id.ib_dislike);
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        View moreButton = findViewById(R.id.ib_movie_detail);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailActivity();
            }
        });

        View likeButton = findViewById(R.id.ib_like);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        // init favourite movies and settings buttons
        View settingsButton = findViewById(R.id.iv_icon_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });

        View profileButton = findViewById(R.id.iv_icon_user);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileActivity();
            }
        });

        // init context and movies
        mContext = getApplicationContext();
        movies = new LinkedList<>();

        // load data into the cards from api
        loadData();


    }

    // shows more infomration about the movie
    private void openDetailActivity(){
        startDetailActivity = new Intent(this, DetailActivity.class);

        // get all cards
        List<Object> views = mSwipeView.getAllResolvers();

        // abort if there are no cards
        if (views.size() == 0)
            return;

        // get the first card, get movie date, parse to json and transfer it to new activity
        MovieCard mc = (MovieCard) views.get(0);
        Gson gson = new Gson();
        String movieData = gson.toJson(mc.getMovieData());
        startDetailActivity.putExtra(Intent.EXTRA_TEXT, movieData );

        // start the new activity
        startActivity(startDetailActivity);
    }

    private void openSettingsActivity(){
        startSettingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(startSettingsActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openProfileActivity(){
        Log.d("TINDER", String.join(",", FilterCriteria.getInstance(mContext).getGenre()));

        startListActivity = new Intent(this, ListActivity.class);
        startActivity(startListActivity);
    }

    private List<Integer> getCurrentCardIds(){
        List<Integer> card_ids = new ArrayList<>();

        List<Object> views = mSwipeView.getAllResolvers();
        for (Object mco : views) {
            MovieCard mc = (MovieCard) mco;
            card_ids.add(mc.getMovieData().id);
        }
        return card_ids;
    }

    // check if internet available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // load new data fom the API
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData() {

        // get parameters
        Map<String, String> parameter = new HashMap<>();
        parameter.put("language", "de");
        parameter.put("sort_by", "popularity.desc");
        parameter.put("include_adult", "false");
        parameter.put("include_video", "false");
        parameter.put("year", String.valueOf(FilterCriteria.getInstance(mContext).getReleaseYear()));
        parameter.put("vote_average.lte", String.valueOf(FilterCriteria.getInstance(mContext).getImdbMaxRating()));
        parameter.put("vote_average.gte", String.valueOf(FilterCriteria.getInstance(mContext).getImdbMinRating()));
        parameter.put("with_genres", String.join(",", FilterCriteria.getInstance(mContext).getGenre()) );
        parameter.put("page", String.valueOf(PAGE_NUMBER));

        // get base URL depending on type (movie or series)
        String base_url = "https://api.themoviedb.org/3/discover/";
        if (FilterCriteria.getInstance(mContext).getType().equals("series"))
            base_url += "tv";
        else
            base_url += "movie";

        // build the URL with parameters
        final String apiUrl = NetworkUtils.buildUrl(base_url, parameter);
        final MainActivity callbackActivity = this;

        // get all liked movie ids from firebase database
        FirebaseHandler.getInstance().getAllLikedMovies(new FirebaseHandler.OnGetDataListener() {
        @Override
        public void onSuccess(DataSnapshot dataSnapshot) {
            movies_ids.clear();
            for(DataSnapshot data: dataSnapshot.getChildren()){
                MovieModel m = data.getValue(MovieModel.class);
                movies_ids.add(m.id);
            }

            // get all disliked movie ids from firebase database
            FirebaseHandler.getInstance().getAllDisikedMovies(new FirebaseHandler.OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        movies_ids.add(Integer.parseInt(data.getKey()));
                    }

                    // asynchronous loader
                    Bundle queryBundle = new Bundle();
                    queryBundle.putString(API_URL_EXTRA, apiUrl);
                    LoaderManager loaderManager = getSupportLoaderManager();
                    Loader<String> apiLoader = loaderManager.getLoader(LOADER_ID);
                    if(apiLoader == null){
                        loaderManager.initLoader(LOADER_ID, queryBundle, callbackActivity);
                    } else{
                        loaderManager.restartLoader(LOADER_ID, queryBundle, callbackActivity);
                    }
                }

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}
            });
        }

        @Override
        public void onStart() {
            Log.d("TINDER", "Start getting data from firebase...");
        }

        @Override
        public void onFailure() {
        }
    });
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
                // get the url from extras
                String apiUrlString = args.getString(API_URL_EXTRA);

                // if url error
                if (apiUrlString.isEmpty()) return null;
                PAGE_NUMBER++;

                // try to get data from the API
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLoadFinished(Loader<String> loader, final String responseData) {

        // checks if the data is empty
        if(responseData == null){
            showErrorToast(getString(R.string.error_msg_api_error));
        } else{
            try {
                // try to get movies from data
                parseDataToMovies(responseData);
            } catch (JSONException e) {
                e.printStackTrace();
                showErrorToast(getString(R.string.error_msg_parsing_error));
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void parseDataToMovies(String apiResult) throws JSONException {
        if(apiResult == null)
            return;

        // Parse to object
        JSONObject jsonObject = new JSONObject(apiResult);

        System.out.println(jsonObject.toString());

        // Parse to array of cards
        JSONArray moviesArray = jsonObject.getJSONArray("results");

        // If page is empty, send new request to first page
        if(moviesArray.length() == 0){
            Log.d("TINDER", "Keine Filme mehr!");
            return;
        }

        // get all current card ids
        List<Integer> cardIds = getCurrentCardIds();

        // Generate each card and add it to the list
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject jsonCard = moviesArray.getJSONObject(i);

            String title, releaseDate = null;
            String type = FilterCriteria.getInstance(mContext).getType();

            if (type.equals("series")) {
                // check if movie has a name
                if (jsonCard.isNull("name")) {
                    // if not, don't add it to the list
                    continue;
                }

                title = jsonCard.getString("name");
                releaseDate = jsonCard.getString("first_air_date");
            }
            else {
                if (jsonCard.isNull("title")) {
                    // if not, don't add it to the list
                    continue;
                }

                title = jsonCard.getString("title");
                releaseDate = jsonCard.getString("release_date");
            }

            // get the remaining info for the movie
            int id = jsonCard.getInt("id");
            String description = jsonCard.getString("overview");
            double vote_average = jsonCard.getDouble("vote_average");

            // check if movie is unknown
            if (movies_ids.contains(id) || cardIds.contains(id)) {
                // if so, don't add it to the list
                continue;
            }

            // check if movie has a poster url
            if (jsonCard.isNull("poster_path")) {
                // if not, don't add it to the list
                continue;
            }
            String poster_url = jsonCard.getString("poster_path");

            // create movie data model with the info
            MovieModel movie = new MovieModel(id, type, title, description, vote_average, poster_url, releaseDate);

            // add string genres
            JSONArray genres = jsonCard.getJSONArray("genre_ids");
            for (int j = 0; j < genres.length(); j++) {
                movie.addGenre(genres.getInt(j));
            }

            // add a genre if no genre is provided
            if (movie.genres.size() == 0)
                movie.genres.add(mContext.getString(R.string.movie_genre_label_unknown));

            // add the movie to the Swipe Cards
            mSwipeView.addView(new MovieCard(movie, mContext, mSwipeView));
        }

        // load more because there are too few, maybe even no cards
        if (getCurrentCardIds().size() < 10) {
            loadData();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        /*
         * We aren't using this method in our tinder app, but we are required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
    }

    private void showErrorToast(String error_msg) {
        // shows a error toast on the screen
        Context context = mContext;
        CharSequence text = error_msg;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        // check if user still logged in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivity(intent);
        }

        // check if settings have changed
        if (FilterCriteria.getInstance(mContext).getChangedState()) {
            mSwipeView.removeAllViews();
            loadData();
        }
    }
}
