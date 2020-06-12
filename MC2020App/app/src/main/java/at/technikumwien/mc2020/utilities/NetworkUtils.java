package at.technikumwien.mc2020.utilities;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class NetworkUtils {


    private static final String API_KEY = "db94b1f559af23f5a8bd53a8dbec0c1e";

    public static String buildUrl(String urlString, Map<String, String> parameter) {
        parameter.put("api_key", API_KEY);

        StringBuilder urlStringBuilder = new StringBuilder(urlString + "?");
        for (Iterator<Map.Entry<String, String>> it = parameter.entrySet().iterator();
             it.hasNext();) {
            Map.Entry<String, String> pair = it.next();

            urlStringBuilder.append(String.format("&%s=%s", pair.getKey(), pair.getValue()));
        }
        urlString = urlStringBuilder.toString();

        return urlString;

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
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A"); // for start of string
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
