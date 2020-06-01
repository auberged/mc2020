package at.technikumwien.mc2020;

import android.net.Uri;
import android.util.Log;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import at.technikumwien.mc2020.ui.utilities.NetworkUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        System.out.println("Test");

        URL apiUrl = NetworkUtils.buildUrl(4);
        System.out.println(apiUrl.toString());

        try {
            //URL apiUrl = new URL(apiUrlString);
            String apiResult = NetworkUtils.getResponseFromHttpUrl(apiUrl);
            System.out.println(apiResult);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //assertEquals(4, 2 + 2);
    }


}