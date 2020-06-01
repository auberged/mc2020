package at.technikumwien.mc2020;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import at.technikumwien.mc2020.utilities.MovieModel;
import at.technikumwien.mc2020.utilities.NetworkUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() throws JSONException {

        // image: https://image.tmdb.org/t/p/w600_and_h900_bestv2/dCdTAOxkcNnsFVKHQnxbklvGRzF.jpg

        URL apiUrl = NetworkUtils.buildUrl("https://api.themoviedb.org/3/discover/movie?api_key=db94b1f559af23f5a8bd53a8dbec0c1e&language=de&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&year=2019", 5);
        System.out.println(apiUrl.toString());

        String apiResult = null;
        try {
            apiResult = NetworkUtils.getResponseFromHttpUrl(apiUrl);
            System.out.println(apiResult);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        List<MovieModel> movies = parseDataToMovies(apiResult);
        if (movies == null) {
            // TODO show error
        }

        for (MovieModel movie : movies ) {
            System.out.println(movie.id + ": " + movie.title + " - " + movie.poster_url);
        }



        //assertEquals(4, 2 + 2);
    }

    public List<MovieModel> parseDataToMovies(String apiResult) throws JSONException {
        if(apiResult == null)
            return null;

        // Parse to object
        JSONObject jsonObject = new JSONObject(apiResult);

        // Parse to array of cards
        JSONArray moviesArray = jsonObject.getJSONArray("results");

        // If page is empty, send new request to first page
        if(moviesArray.length() == 0){
            return null;
        }

        // Make a new empty list
        List<MovieModel> movies = new LinkedList<>();

        // Generate each card and add it to the list
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject jsonCard = moviesArray.getJSONObject(i);
            int id = jsonCard.getInt("id");
            String title = jsonCard.getString("title");
            String description = jsonCard.getString("overview");
            double vote_average = jsonCard.getDouble("vote_average");
            String poster_url = jsonCard.getString("poster_path");
            String releaseDate = jsonCard.getString("release_date");

            MovieModel movie = new MovieModel(id, title, description, vote_average, poster_url, releaseDate);

            JSONArray genres = jsonCard.getJSONArray("genre_ids");
            for (int j = 0; j < genres.length(); j++) {
                movie.addGenre(genres.getString(j));
            }

            movies.add(movie);
        }

        return movies;

    }


}