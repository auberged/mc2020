package at.technikumwien.mc2020;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import at.technikumwien.mc2020.utilities.MovieModel;


/**
 * Inspired by https://blog.mindorks.com/android-tinder-swipe-view-example-3eca9b0d4794
 * See step 9
 */
@Layout(R.layout.movie_card_layout)
public class MovieCard {

    @View(R.id.iv_movie_poster_swipe)
    private ImageView profileImageView;

    private String ImageUrl; // Replace Image with the Movie Class later....
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private MovieModel movieData;

    public MovieCard(String ImageUrl, Context mContext, SwipePlaceHolderView mSwipeView) {
        this.ImageUrl = ImageUrl;
        this.mContext = mContext;
        this.mSwipeView = mSwipeView;
    }

    public MovieCard(MovieModel movie, Context mContext, SwipePlaceHolderView mSwipeView) {
        this.ImageUrl = movie.poster_url;
        this.movieData = movie;
        this.mContext = mContext;
        this.mSwipeView = mSwipeView;

    }

    @Resolve
    private void onResolved(){
        // Load here the Movie Poster into the layout
        Glide.with(mContext).load(ImageUrl).into(profileImageView);
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("TINDER", "Disliked movie: " + movieData.title);
        //mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("TINDER", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){

        Log.d("TINDER", "Liked movie: " + movieData.title);
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("TINDER", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("TINDER", "onSwipeOutState");
    }
}
