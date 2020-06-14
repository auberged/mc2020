package at.technikumwien.mc2020.ui.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mindorks.placeholderview.InfinitePlaceHolderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import at.technikumwien.mc2020.MovieCard;
import at.technikumwien.mc2020.R;
import at.technikumwien.mc2020.data.database.Constants;
import at.technikumwien.mc2020.utilities.MovieModel;

public class DetailActivity extends AppCompatActivity {

    private MovieModel movie;

    private TextView title;
    private TextView rating;
    private TextView year;
    private TextView genres;
    private TextView description;
    private ImageView posterImage;

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = (TextView) findViewById(R.id.tv_movie_title);
        rating = (TextView) findViewById(R.id.tv_movie_rating);
        year = (TextView) findViewById(R.id.tv_movie_year);
        genres = (TextView) findViewById(R.id.tv_movie_categories);
        description = (TextView) findViewById(R.id.tv_movie_description);
        posterImage = (ImageView) findViewById(R.id.iv_movie_poster_swipe);

        Intent intent = getIntent();

        // check for extra data
        if (intent.hasExtra(Intent.EXTRA_TEXT) ) {
            String data = intent.getStringExtra(Intent.EXTRA_TEXT);

            // TODO Hier die Daten setzen in der View
            Log.d("TINDER", data);
            Gson gson = new Gson();
            movie = gson.fromJson(data, MovieModel.class);

            title.setText(movie.title);
            rating.setText(String.format(getString(R.string.rating_text), movie.vote_average));
            year.setText(movie.releaseDate);

            // TODO max ersten 2 genre nehmen
            int k = movie.genres.size();
            if ( k > 2 )
                movie.genres.subList(2, k).clear();


            genres.setText(TextUtils.join(", ",movie.genres));
            title.setText(movie.title);
            description.setText(movie.description);

            Picasso.get().load(movie.poster_url).into(posterImage);

            ImageView shareButton = findViewById(R.id.iv_icon_share);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareMovie();
                }
            });


        }


    }

    private void shareMovie() {
        Log.d("TINDER", "share");
        //showErrorToast();

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shareMovieSubject));
        String shareText = getString(R.string.shareMovieDescription) + movie.title;
        share.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(share, getString(R.string.shareMovieTitle)));

    }
}
