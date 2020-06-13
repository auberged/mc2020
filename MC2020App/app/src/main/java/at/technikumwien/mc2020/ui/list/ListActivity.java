package at.technikumwien.mc2020.ui.list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.InfinitePlaceHolderView;

import java.util.ArrayList;
import java.util.List;

import at.technikumwien.mc2020.R;
import at.technikumwien.mc2020.data.database.Constants;
import at.technikumwien.mc2020.utilities.MovieModel;

public class ListActivity extends AppCompatActivity {

    private InfinitePlaceHolderView likedMoviesView;
    private static int LOAD_MOVIE_COUNT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        likedMoviesView = findViewById(R.id.Liked_movies_placeholder_view);


        likedMoviesView.getBuilder()
                .setHasFixedSize(false)
                .setItemViewCacheSize(LOAD_MOVIE_COUNT)
                .setLayoutManager(new LinearLayoutManager(
                        getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        false));

        likedMoviesView.setLoadMoreResolver(new LoadMoreView(new LoadMoreView.Callback(){
            @Override
            public void onShowMore(){

                int index = likedMoviesView.getViewAdapter().getItemCount();
                MovieItem m = (MovieItem) likedMoviesView.getViewResolverAtPosition(index - 2);
                final String startNode = String.valueOf(m.getMovieModel().id);

                FirebaseDatabase.getInstance().getReference().child(Constants.LIKED_MOVIES).child(FirebaseAuth.getInstance().getUid()).orderByKey().startAt(startNode).limitToFirst(LOAD_MOVIE_COUNT).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<MovieModel> movies = new ArrayList<>();
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            MovieModel m = data.getValue(MovieModel.class);
                            if(!String.valueOf(m.id).equals(startNode)) {
                                likedMoviesView
                                        .addView(new MovieItem(getApplicationContext(), m));
                            }
                        }
                        //fetch more data and then call
                        likedMoviesView.loadingDone();

                        //if all the data is fetch then call
                        if(dataSnapshot.getChildrenCount() <= 1){
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.reached_end_of_favourite_movie_list, Toast.LENGTH_LONG);
                            toast.show();
                            likedMoviesView.noMoreToLoad();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.error_loading_favourite_movies, Toast.LENGTH_LONG);
                        toast.show();
                        likedMoviesView.loadingDone();
                    }
                });
            }
        }));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    Drawable deleteIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.delete_icon_18);
                    int intrinsicWidth = deleteIcon.getIntrinsicWidth();
                    int intrinsicHeight = deleteIcon.getIntrinsicHeight();

                    View itemView = viewHolder.itemView;
                    int itemHeight = itemView.getBottom() - itemView.getTop();

                    // Draw the red delete background
                    final ColorDrawable background = new ColorDrawable();
                    background.setColor(Color.parseColor("#f44336"));
                    background.setBounds(
                            itemView.getRight() + Float.floatToIntBits(dX),
                            itemView.getTop(),
                            itemView.getRight(),
                            itemView.getBottom()
                    );
                    background.draw(c);

                    // Calculate position of delete icon
                    int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                    int iconMargin = (itemHeight - intrinsicHeight) / 2;
                    int iconLeft = itemView.getRight() - iconMargin - intrinsicWidth;
                    int iconRight = itemView.getRight() - iconMargin;
                    int iconBottom = iconTop + intrinsicHeight;

                    // Draw the delete icon
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    deleteIcon.draw(c);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast toast = Toast.makeText(getApplicationContext(), "Gelöscht", Toast.LENGTH_LONG);
                toast.show();
                likedMoviesView.removeViewAt(viewHolder.getAdapterPosition());
                likedMoviesView.refresh();

                // TODO: Item aus Firebase löschen.
            }
        }).attachToRecyclerView(likedMoviesView);

        FirebaseDatabase.getInstance().getReference().child(Constants.LIKED_MOVIES).child(FirebaseAuth.getInstance().getUid()).orderByKey().limitToFirst(LOAD_MOVIE_COUNT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MovieModel> movies = new ArrayList<>();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    MovieModel m = data.getValue(MovieModel.class);
                    likedMoviesView
                            .addView(new MovieItem(getApplicationContext(), m));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_loading_favourite_movies, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
