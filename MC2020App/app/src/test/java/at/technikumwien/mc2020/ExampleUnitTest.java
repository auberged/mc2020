package at.technikumwien.mc2020;

import org.junit.Test;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() throws IOException {
        System.out.println("Test");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?api_key=db94b1f559af23f5a8bd53a8dbec0c1e&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&year=2019")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());

        /*
        try {
            URL apiUrl = new URL("https://api.magicthegathering.io/v1/cards");
            String apiResult = NetworkUtils.getResponseFromHttpUrl(apiUrl);
            System.out.println(apiResult);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        */


        //assertEquals(4, 2 + 2);
    }


}