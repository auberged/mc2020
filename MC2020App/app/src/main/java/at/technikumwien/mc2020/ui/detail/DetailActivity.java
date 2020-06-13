package at.technikumwien.mc2020.ui.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        // check for extra data
        if (intent.hasExtra(Intent.EXTRA_TEXT) ) {
            String data = intent.getStringExtra(Intent.EXTRA_TEXT);

            // TODO Hier die Daten setzen in der View
            Log.d("TINDER", data);

        }

    }
}
