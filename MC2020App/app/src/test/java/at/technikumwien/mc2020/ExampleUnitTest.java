package at.technikumwien.mc2020;

import android.text.TextUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {


    @Test
    public void console_tester() {

        List<String> tempGenres = new ArrayList<>();
        tempGenres.add("Komödie");
        tempGenres.add("Action");
        tempGenres.add("Liebes");
        tempGenres.add("Western");

        System.out.println(tempGenres.toString());

        String genreText = "";
        for (String genre : tempGenres) {
            if (genreText.length() + genre.length() <= 23)
                genreText += genre + ", ";
        }
        genreText = genreText.substring(0, genreText.length() -2);
        System.out.println(genreText);


        assertEquals(4, 2 + 2);
    }

}