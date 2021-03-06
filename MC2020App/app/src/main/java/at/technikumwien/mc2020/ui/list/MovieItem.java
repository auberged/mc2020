package at.technikumwien.mc2020.ui.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import com.google.gson.Gson;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.LongClick;
import com.mindorks.placeholderview.annotations.Position;
import com.mindorks.placeholderview.annotations.Recycle;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.squareup.picasso.Picasso;

import java.util.List;

import at.technikumwien.mc2020.R;
import at.technikumwien.mc2020.ui.detail.DetailActivity;
import at.technikumwien.mc2020.utilities.MovieModel;

@Layout(R.layout.load_movie_item_view)
public class MovieItem {

    @View(R.id.card_view)
    CardView cardView;

    @View(R.id.image_movie_view)
    ImageView imageView;

    @View(R.id.movie_title)
    TextView movieTitle;

    @View(R.id.movie_rating)
    TextView movieRating;

    @View(R.id.movie_categories)
    TextView movieCategories;

    @View(R.id.movie_description)
    TextView movieDescription;

    @Position
    int position;

    private MovieModel movieModel;
    private Context context;
    private Intent startDetailActivity;


    public MovieItem(Context context, MovieModel movieModel) {
        this.context = context;
        this.movieModel = movieModel;
    }

    /*
     * This method is called when the view is rendered
     * onResolved method could be named anything, Example: onAttach
     */
    @Resolve
    public void onResolved() {
        // do something here
        // example: load imageView with url image
        Picasso.get().load(movieModel.poster_url).into(imageView);

        movieTitle.setText(movieModel.title);
        movieRating.setText(String.format("%s/10 Sterne", movieModel.vote_average));

        List<String> tempGenres = movieModel.genres;
        if ( tempGenres != null && tempGenres.size() > 2 ) {
            tempGenres = movieModel.genres.subList(0, 2);
            movieCategories.setText(TextUtils.join(", ",tempGenres));
        }

        // String shortening
        String shortendDescription = movieModel.description.substring(0, Math.min(movieModel.description.length(), 60));
        shortendDescription += "...";
        movieDescription.setText(shortendDescription);

    }

    /*
     * This method is called when the view holder is recycled
     * and used to display view for the next data set
     */
    @Recycle
    public void onRecycled() {
    }


    /*
     * This method is called when the view with id image_view is clicked.
     * onImageViewClick method could be named anything.
     */
    @Click(R.id.cl_single_liked_movie)
    public void onImageViewClick() {
        // create detail intent
        startDetailActivity = new Intent(context, DetailActivity.class);
        startDetailActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // get movie data and dave it into extra
        Gson gson = new Gson();
        String movieData = gson.toJson(movieModel);
        startDetailActivity.putExtra(Intent.EXTRA_TEXT, movieData );

        // start the detail activity
        context.startActivity(startDetailActivity);
    }

    @LongClick(R.id.image_movie_view)
    public void onImageViewLongClick() {
        // do something
    }

    public MovieModel getMovieModel(){
        // get the movie data
        return this.movieModel;
    }
}
