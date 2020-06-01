package at.technikumwien.mc2020.ui.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static String API_ENDPOINT = "https://api.magicthegathering.io/v1/cards";


    public static URL buildUrl(int pageNr) {
        Uri tempUri = Uri.parse(API_ENDPOINT).buildUpon().
                appendQueryParameter("page", Integer.toString(pageNr)).build();

        URL url = null;

        try {
            url = new URL(tempUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.d("TOAST", url.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            Log.d("TOAST", "here_A");
            InputStream in = urlConnection.getInputStream();
            Log.d("TOAST", "here_B");

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A"); // for start of string

            Log.d("TOAST", "here_C");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }


}
