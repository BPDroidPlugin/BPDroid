package com.example.clientjava;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogUtil {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LOG_FILE_NAME = "TestBattery.txt";

    public static void logInfo(Context context, String message) {
        Log.i(TAG, message);

        writeLogToFile(context, message);
    }

    private static void writeLogToFile(Context context, String message) {
        //File logFile = new File(context.getExternalFilesDir(null), LOG_FILE_NAME);
        File logFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), LOG_FILE_NAME);
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.append(message).append("\n");
        } catch (IOException e) {
            Log.e(TAG, "Error writing log to file", e);
        }
    }
}
