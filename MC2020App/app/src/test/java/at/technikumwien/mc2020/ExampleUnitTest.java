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
    public void addition_isCorrect() {

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

        //parseDataToMovies(apiResult);



        //assertEquals(4, 2 + 2);
    }

    public List<MovieModel> parseDataToMovies(String apiResult) throws JSONException {
        if(apiResult == null)
            return null;

        // Parse to object
        JSONObject jsonObject = new JSONObject(apiResult);

        // Parse to array of cards
        JSONArray cardsArray = jsonObject.getJSONArray("results");

        // If page is empty, send new request to first page
        if(cardsArray.length() == 0){
            // TODO add correct end condition (e.g. page = page in response)
            return null;
        }

        // Make a new empty list
        List<MovieModel> movies = new LinkedList<MovieModel>();

        // Generate each card and add it to the list
        for (int i = 0; i < cardsArray.length(); i++) {
            JSONObject jsonCard = cardsArray.getJSONObject(i);
            String name = jsonCard.getString("name");

            MovieModel movie = new MovieModel(name, type, rarity);

            JSONArray colors = jsonCard.getJSONArray("colors");
            for (int j = 0; j < colors.length(); j++) {
                movie.addColor(colors.getString(j));
            }

            movies.add(movie);
        }

        return movies;

    }


}