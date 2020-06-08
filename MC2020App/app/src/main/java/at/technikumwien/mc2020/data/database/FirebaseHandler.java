package at.technikumwien.mc2020.data.database;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.technikumwien.mc2020.utilities.MovieModel;

public class FirebaseHandler {
    private static FirebaseHandler firebaseHandler;

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    private FirebaseHandler(){

    }

    public static FirebaseHandler getInstance() {
        if (firebaseHandler == null) {
            synchronized (LOCK) {
                firebaseHandler = new FirebaseHandler();
            }
        }
        return firebaseHandler;
    }

    private String currentUser(){
        return FirebaseAuth.getInstance().getUid();
    }

    public void saveLikedMovie(MovieModel movie){
        String userId = currentUser();

        FirebaseDatabase.getInstance().getReference().child(Constants.LIKED_MOVIES).child(userId).child(String.valueOf(movie.id)).setValue(movie);
    }

    public void saveDislikedMovie(MovieModel movie){
        String userId = currentUser();

        FirebaseDatabase.getInstance().getReference().child(Constants.DISLIKED_MOVIES).child(userId).child(String.valueOf(movie.id)).setValue(movie.title);
    }

    public List<MovieModel> getAllLikedMovies(){
        String userId = currentUser();
        final List<MovieModel> movies = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child(Constants.LIKED_MOVIES).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){

                    MovieModel m = data.getValue(MovieModel.class);
                    movies.add(m);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return movies;
    }

    /*public MovieModel getMovieDetails(String movieId){
        String userId = currentUser();
        final MovieModel movie = null;

        FirebaseDatabase.getInstance().getReference().child(Constants.LIKED_MOVIES).child(userId).child(movieId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movie = dataSnapshot.getValue(MovieModel.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Aufrufe laufen asynchron ab. Daher kann es sein, dass man hier auf einen null Wert zurückliefert. Aus diesem Grund sollten der Firebase Aufruf immer direkt, wo er in den Activities benötigt wird, aufgerufen werden.
        return movie[0];
    }*/

    // Movie getMovieDetails

    // List<Integer> getAllDislikedMovieIds
}
