    package com.example.clientjava;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyTest {

    private LazyFileReader lazyReader;
    private LazyFileReader lazyReaderPic;

    @Before
    public void setUp() throws IOException {
        // Initialize LazyFileReader with the path to the file on your laptop
        //adb push /path/to/your/random_strings.txt /data/local/tmp/random_strings.txt
        String filePath = "/data/local/tmp/random_strings.txt";  // Make sure to use the correct path here
        //adb push /path/to/your/random_integers.txt /data/local/tmp/random_integers.txt
        String picFilePath = "/data/local/tmp/random_integers.txt";
        lazyReader = new LazyFileReader(filePath);
        lazyReaderPic = new LazyFileReader(picFilePath);
    }


    @Test
    public void testHTTP() throws IOException {

        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.btnBatteryStart)).perform(click());
        SystemClock.sleep(1000);


        for(int i = 0; i < 150; i++){

            SystemClock.sleep(1000);
            closeSoftKeyboard();

            onView(withId(R.id.btnSendGet)).perform(click());
            SystemClock.sleep(1000);

            String text = lazyReader.readNextLine();
            if(text == null){
                text = "Fallback text!"; // In case the file runs out of lines, provide a fallback string.
            }
            onView(withId(R.id.editTextUserData)).perform(clearText(), typeText(text));
            SystemClock.sleep(1000);
            closeSoftKeyboard();

            onView(withId(R.id.btnSendData)).perform(click());
            SystemClock.sleep(1000);

            String picName = lazyReaderPic.readNextLine();
            if(picName == null){
                picName = "38"; // In case the file runs out of lines, provide a fallback string.
            }
            onView(withId(R.id.editTextPicNumber)).perform(clearText(), typeText(picName));
            SystemClock.sleep(1000);
            closeSoftKeyboard();

            onView(withId(R.id.btnGetPic)).perform(click());
            SystemClock.sleep(1000);

            onView(withId(R.id.imageViewPicture)).check(matches(isDisplayed()));
            SystemClock.sleep(1000);

            onView(withId(R.id.btnClearImage)).perform(click());
            SystemClock.sleep(1000);

        }

        onView(withId(R.id.btnBatteryEnd)).perform(click());
        SystemClock.sleep(1000);

        activityScenario.close();

    }

    @After
    public void tearDown() throws IOException {
        // Close the reader after the test is complete
        if (lazyReader != null) {
            lazyReader.close();
        }

        if (lazyReaderPic != null) {
            lazyReaderPic.close();
        }
    }


    // LazyFileReader class should be inside the test as well or in a separate file
    public static class LazyFileReader {
        private BufferedReader reader;
        private String currentLine;

        // Constructor that initializes the reader with the file path
        public LazyFileReader(String filePath) throws IOException {
            reader = new BufferedReader(new FileReader(filePath));
            currentLine = null; // Initially no line has been read
        }

        // Method that returns the next line each time it's called
        public String readNextLine() throws IOException {
            // Read the next line
            currentLine = reader.readLine();

            // Return the current line or null if end of file
            return currentLine;
        }

        // Method to close the reader when done
        public void close() throws IOException {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
