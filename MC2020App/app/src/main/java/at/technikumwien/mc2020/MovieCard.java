package at.technikumwien.mc2020;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;


import at.technikumwien.mc2020.data.database.Constants;
import at.technikumwien.mc2020.data.database.FirebaseHandler;
import at.technikumwien.mc2020.utilities.MovieModel;


/**
 * Inspired by https://blog.mindorks.com/android-tinder-swipe-view-example-3eca9b0d4794
 * See step 9
 */
@NonReusable
@Animate(Animate.CARD_BOTTOM_IN_ASC)
@Layout(R.layout.movie_card_layout)
public class MovieCard {

    @View(R.id.iv_movie_poster_swipe)
    public ImageView profileImageView;

    private String ImageUrl;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private MovieModel movieData;

    public MovieCard(MovieModel movie, Context mContext, SwipePlaceHolderView mSwipeView) {
        this.ImageUrl = movie.poster_url;
        this.movieData = movie;
        this.mContext = mContext;
        this.mSwipeView = mSwipeView;
    }

    // returns the data of the movie
    public MovieModel getMovieData() {
        return movieData;
    }

    @Resolve
    public void onResolved() {
        // Load here the Movie Poster into the layout
        Glide.with(mContext).load(ImageUrl).into(profileImageView);
    }

    @SwipeOut
    public void onSwipedOut(){
        // user disliked the movie
        FirebaseHandler.getInstance().saveDislikedMovie(movieData);
        Log.d("TINDER", "Disliked movie: " + movieData.title +  " " + movieData.poster_url);
    }

    @SwipeIn
    public void onSwipeIn(){
        // user liked the movie
        FirebaseHandler.getInstance().saveLikedMovie(movieData);
        Log.d("TINDER", "Liked movie: " + movieData.title +  " " + movieData.poster_url);
    }





    // Not used states

    @SwipeCancelState
    public void onSwipeCancelState() {
        //Log.d("TINDER", "onSwipeCancelState");
    }

    @SwipeInState
    public void onSwipeInState(){
        //Log.d("TINDER", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState(){
        //Log.d("TINDER", "onSwipeOutState");
    }
}
