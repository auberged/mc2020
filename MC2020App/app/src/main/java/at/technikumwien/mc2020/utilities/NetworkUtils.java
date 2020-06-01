package at.technikumwien.mc2020.utilities;

import android.net.Uri;
import android.util.Log;

import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public static URL buildUrl(String urlString, int pageNr) {
        URIBuilder b = null;
        try {
            b = new URIBuilder(urlString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        b.addParameter("page", String.valueOf(pageNr));

        URL url = null;

        try {
            url = b.build().toURL();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;

        /*
        Uri tempUri = Uri.parse(urlString).buildUpon().appendQueryParameter("page", Integer.toString(pageNr)).build();
        URL url = null;

        try {
            url = new URL(tempUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

        */
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
