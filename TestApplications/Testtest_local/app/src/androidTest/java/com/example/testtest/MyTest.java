package com.example.testtest;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyTest {

    private LazyFileReader lazyReader;

    @Before
    public void setUp() throws IOException {
        // Initialize LazyFileReader with the path to the file on your laptop
        //adb push /path/to/your/random_strings.txt /data/local/tmp/random_strings.txt
        String filePath = "/data/local/tmp/random_strings.txt";  // Make sure to use the correct path here
        lazyReader = new LazyFileReader(filePath);

//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
//
//        if (batteryManager != null) {
//            // Get battery level as a percentage
//            int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//
//            // Request storage permissions
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
//                        "appops set " + InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName() +
//                                " MANAGE_EXTERNAL_STORAGE allow"
//                );
//            }
//
//            //File logFile = new File(context.getExternalFilesDir(null), LOG_FILE_NAME);
//            File logFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "TestBattery.txt");
//            try (FileWriter writer = new FileWriter(logFile, true)) {
//                writer.append("Battery Level at Start: " + batteryLevel ).append("\n");
//            } catch (IOException e) {
//                throw new IOException("I/O ERROR in SetUp");
//            }
//        } else {
//            throw new RuntimeException("BatteryManager is not available");
//        }
    }

    @Test
    public void testSendBtn2() throws IOException {
        Random rand = new Random();

        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.btn_first)).perform(click());
        SystemClock.sleep(1000);


        for(int i = 0; i < 100; i++){

            //ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

            //String text = Utils.generateRandomString(rand.nextInt(25));
            String text = lazyReader.readNextLine();
            if (text == null) {
                text = "Fallback text"; // In case the file runs out of lines, provide a fallback string.
            }
            onView(withId(R.id.editText_main)).perform(typeText(text));

            SystemClock.sleep(1000);
            closeSoftKeyboard();

            onView(withId(R.id.button_main)).perform(click());

            SystemClock.sleep(1000);
            closeSoftKeyboard();

            onView(withId(R.id.second)).check(matches(isDisplayed()));

            SystemClock.sleep(1000);

            //String textBack = Utils.generateRandomString(rand.nextInt(20));
            String textBack = lazyReader.readNextLine();
            if (textBack == null) {
                textBack = "Fallback text";
            }
            onView(withId(R.id.editText_second)).perform(typeText(textBack));
            closeSoftKeyboard();
            SystemClock.sleep(1000);

            onView(withId(R.id.button_second)).perform(click());

            onView(withId(R.id.main)).check(matches(isDisplayed()));

            onView(withId(R.id.editText_main)).perform(clearText());

            closeSoftKeyboard();

            SystemClock.sleep(1000);

            //activityScenario.close();
        }

        onView(withId(R.id.btn_end)).perform(click());
        SystemClock.sleep(1000);

        activityScenario.close();

    }

    @After
    public void tearDown() throws IOException {
        // Close the reader after the test is complete
        if (lazyReader != null) {
            lazyReader.close();
        }

//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
//
//        if (batteryManager != null) {
//            // Get battery level as a percentage
//            int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//
//            // Request storage permissions
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
//                        "appops set " + InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName() +
//                                " MANAGE_EXTERNAL_STORAGE allow"
//                );
//            }
//
//            //File logFile = new File(context.getExternalFilesDir(null), LOG_FILE_NAME);
//            File logFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "TestBattery.txt");
//            try (FileWriter writer = new FileWriter(logFile, true)) {
//                writer.append("Battery Level at End: " + batteryLevel ).append("\n");
//            } catch (IOException e) {
//                throw new IOException("I/O ERROR in tearDown");
//            }
//        } else {
//            throw new RuntimeException("BatteryManager is not available");
//        }
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
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
//
//
//
//    @Test
//    public void testSendBtn() {
//        onView(withId(R.id.editText_main)).perform(typeText("Hello"));
//        onView(withId(R.id.button_main)).perform(click());
//
//        onView(withText(R.id.text_message)).check(ViewAssertions.matches(withText("Helloy")));
//    }
}
