package at.technikumwien.mc2020;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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


/**
 * Inspired by https://blog.mindorks.com/android-tinder-swipe-view-example-3eca9b0d4794
 * See step 9
 */
@Layout(R.layout.movie_card_layout)
public class MovieCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    private String ImageUrl; // Replace Image with the Movie Class later....
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public MovieCard(String ImageUrl, Context mContext, SwipePlaceHolderView mSwipeView) {
        this.ImageUrl = ImageUrl;
        this.mContext = mContext;
        this.mSwipeView = mSwipeView;
    }

    @Resolve
    private void onResolved(){
        //Glide.with(mContext).load(ImageUrl).into(profileImageView);
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}
