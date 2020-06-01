package at.technikumwien.mc2020;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import at.technikumwien.mc2020.utilities.NetworkUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() {
        System.out.println("Test");

        URL apiUrl = NetworkUtils.buildUrl("https://api.themoviedb.org/3/discover/movie?api_key=db94b1f559af23f5a8bd53a8dbec0c1e&language=de&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&year=2019", 5);
        System.out.println(apiUrl.toString());

        // image: https://image.tmdb.org/t/p/w600_and_h900_bestv2/dCdTAOxkcNnsFVKHQnxbklvGRzF.jpg

        try {
            String apiResult = NetworkUtils.getResponseFromHttpUrl(apiUrl);
            System.out.println(apiResult);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }



        //assertEquals(4, 2 + 2);
    }


}