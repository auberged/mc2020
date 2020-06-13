package at.technikumwien.mc2020.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
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
import at.technikumwien.mc2020.ui.launcher.LauncherActivity;
import at.technikumwien.mc2020.ui.settings.SettingsActivity;
import at.technikumwien.mc2020.utilities.FilterCriteria;
import at.technikumwien.mc2020.utilities.MovieModel;
import at.technikumwien.mc2020.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private Intent startSettingsActivity;
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

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivity(intent);
        }

        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipe_view);
        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemRemoved(int count) {
                Log.d("TINDER", String.valueOf(count));
                if ( (count <= 30) && (count % 5 == 0) || (count <= 50) && (count % 10 == 0)) {
                    Log.d("TINDER", "load more .....");
                    loadData();
                }
            }
        });

        mContext = getApplicationContext();
        movies = new LinkedList<>();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(-50)
                        .setRelativeScale(0.05f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


        //FilterCriteria filterCriteria = FilterCriteria.getInstance (mContext);
        //Log.d("TINDER", filterCriteria.getType());

        loadData();

        //mSwipeView.addView(new MovieCard("https://i.pinimg.com/originals/fd/5e/66/fd5e662dce1a3a8cd192a5952fa64f02.jpg", mContext, mSwipeView));

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
    }

    private void openSettingsActivity(){
        startSettingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(startSettingsActivity);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openProfileActivity(){
        Log.d("TINDER", "share");
        //showErrorToast();

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shareMovieSubject));
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareMovieDescription));

        startActivity(Intent.createChooser(share, getString(R.string.shareMovieTitle)));

        Log.d("TINDER", String.join(",", FilterCriteria.getInstance(mContext).getGenreList()));

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




    //    @Override
    //    protected void onSaveInstanceState(@NonNull Bundle outState) {
    //        super.onSaveInstanceState(outState);
    //        outState.putInt(PAGE_NR_EXTRA, PAGE_NUMBER);
    //    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData() {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("language", "de");
        parameter.put("sort_by", "popularity.desc");
        parameter.put("include_adult", "false");
        parameter.put("include_video", "false");
        parameter.put("year", String.valueOf(FilterCriteria.getInstance(mContext).getReleaseYear()));
        parameter.put("vote_average.lte", String.valueOf(FilterCriteria.getInstance(mContext).getImdbMaxRating()));
        parameter.put("vote_average.gte", String.valueOf(FilterCriteria.getInstance(mContext).getImdbMinRating()));
        // TODO weil genre ids nur bei filmen gehen
        // parameter.put("with_genres", String.join(",", FilterCriteria.getInstance(mContext).getGenreList()) );
        parameter.put("page", String.valueOf(PAGE_NUMBER));

        String base_url = "https://api.themoviedb.org/3/discover/";
        if (FilterCriteria.getInstance(mContext).getType().equals("series"))
            base_url += "tv";
        else
            base_url += "movie";


        final String apiUrl = NetworkUtils.buildUrl(base_url, parameter);
        final MainActivity callbackActivity = this;


        FirebaseHandler.getInstance().getAllLikedMovies(new FirebaseHandler.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                movies_ids.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    MovieModel m = data.getValue(MovieModel.class);
                    movies_ids.add(m.id);
                }

                FirebaseHandler.getInstance().getAllDisikedMovies(new FirebaseHandler.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            movies_ids.add(Integer.parseInt(data.getKey()));
                        }

                        Log.d("TINDER", apiUrl);
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
                    public void onStart() {
                        //Log.d("TINDER", "Start disliking search...");
                    }

                    @Override
                    public void onFailure() {
                    }
                });
            }

            @Override
            public void onStart() {
                //Log.d("TINDER", "Start liking search...");
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
                String apiUrlString = args.getString(API_URL_EXTRA);

                // if url error
                if (apiUrlString.isEmpty()) return null;
                PAGE_NUMBER++;


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
        if(responseData == null){
            showErrorToast(getString(R.string.error_msg_api_error));
        } else{
            try {
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
            return;
        }

        // get all current card ids
        List<Integer> cardIds = getCurrentCardIds();

        // Generate each card and add it to the list
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject jsonCard = moviesArray.getJSONObject(i);

            String title, releaseDate = null;

            if (FilterCriteria.getInstance(mContext).getType().equals("series")) {
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


            int id = jsonCard.getInt("id");
            String description = jsonCard.getString("overview");
            double vote_average = jsonCard.getDouble("vote_average");

            // check if movie is unknown
            if (movies_ids.contains(id) || cardIds.contains(id)) {
                //Log.d("TINDER", title + " already known");
                continue;
            }

            // check if movie has a poster url
            if (jsonCard.isNull("poster_path")) {
                // if not, don't add it to the list
                continue;

            }
            String poster_url = jsonCard.getString("poster_path");

            MovieModel movie = new MovieModel(id, title, description, vote_average, poster_url, releaseDate);

            JSONArray genres = jsonCard.getJSONArray("genre_ids");
            for (int j = 0; j < genres.length(); j++) {
                movie.addGenre(genres.getInt(j));
            }

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
        //Context context = getApplicationContext();
        Context context = mContext;
        CharSequence text = error_msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivity(intent);
        }

        if (FilterCriteria.getInstance(mContext).getChangedState()) {
            mSwipeView.removeAllViews();
            loadData();
        }
    }
}
