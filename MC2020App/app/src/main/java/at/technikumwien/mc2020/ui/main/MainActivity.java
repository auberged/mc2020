package at.technikumwien.mc2020.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import at.technikumwien.mc2020.MovieCard;
import at.technikumwien.mc2020.R;
import at.technikumwien.mc2020.ui.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButton;

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipe_view);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        mSwipeView.addView(new MovieCard("https://i.pinimg.com/originals/fd/5e/66/fd5e662dce1a3a8cd192a5952fa64f02.jpg", mContext, mSwipeView));
        mSwipeView.addView(new MovieCard("https://i.pinimg.com/originals/fd/5e/66/fd5e662dce1a3a8cd192a5952fa64f02.jpg", mContext, mSwipeView));
        mSwipeView.addView(new MovieCard("https://i.pinimg.com/originals/fd/5e/66/fd5e662dce1a3a8cd192a5952fa64f02.jpg", mContext, mSwipeView));
        mSwipeView.addView(new MovieCard("https://i.pinimg.com/originals/fd/5e/66/fd5e662dce1a3a8cd192a5952fa64f02.jpg", mContext, mSwipeView));

        /*imageButton = findViewById(R.id.id_preference_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });*/
    }



    private void openSettingsActivity(){
        Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(startSettingsActivity);
    }
}
