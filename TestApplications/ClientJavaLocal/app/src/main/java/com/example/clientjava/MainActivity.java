package com.example.clientjava;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String serverIP = "193.157.253.102";

    private EditText editTextUserData;

    private TextView txtGetResult;
    private TextView txtPostResult;

    private EditText editTextPicNumber;

    private ImageView imageView;

    private final OkHttpClient client = new OkHttpClient();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageViewPicture);

        txtGetResult = findViewById(R.id.txtGetResult);
        txtPostResult = findViewById(R.id.txtPostResult);

        editTextUserData = findViewById(R.id.editTextUserData);
        editTextPicNumber = findViewById(R.id.editTextPicNumber);

        Log.d("GreenMeter", "(onCreate,MainActivity,METHOD_END)");
    }


    public void sendGetRequest(View view) {
        String serverUrl = "http://" + serverIP + ":6500/test_get";

        executorService.execute(() -> {
            int successCount = 0;
            Request request = new Request.Builder()
                    .url(serverUrl)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> txtGetResult.setText("Get request was successful!"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendPostRequest(View view) {
        String serverUrl = "http://" + serverIP + ":6500/test_post";
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = "{\"key\": \"user\", \"value\": \"" + editTextUserData.getText().toString() + "\"}";
        RequestBody body = RequestBody.create(json, JSON);

        executorService.execute(() -> {
            Request request = new Request.Builder()
                    .url(serverUrl)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String responseText = response.body() != null ? response.body().string() : "No Response Body";
                runOnUiThread(() -> txtPostResult.setText("POST Response: " + responseText));
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> txtPostResult.setText("Error: " + e.getMessage()));
            }
        });
    }

    public void getPicture(View view) {
        int picNumber = Integer.parseInt(editTextPicNumber.getText().toString());

        if(picNumber > 50){
            picNumber = 50;
        }else if(picNumber < 1){
            picNumber = 1;
        }

        String serverUrl = "http://" + serverIP + ":6500/get_picture?number=" + picNumber;

        // Append a unique query parameter to force a fresh fetch
        String uniqueUrl = serverUrl + "&timestamp=" + System.currentTimeMillis();

        runOnUiThread(() -> {
            // Use Picasso to load the image from the server into the ImageView
            Picasso.get()
                    .load(uniqueUrl)
                    //.placeholder(R.drawable.placeholder) // Optional: a placeholder image
                    //.error(R.drawable.error) // Optional: an error image
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE) // Disable memory caching
                    .networkPolicy(NetworkPolicy.NO_CACHE) // Force network request
                    .into(imageView);
        });
    }

    public void clearResults(View view) {
        txtGetResult.setText("");
        txtPostResult.setText("");
        imageView.setImageDrawable(null);
    }

    public void getBatteryLevelAtStart(View view) {
        String level = String.valueOf(getBatteryLevel());
        LogUtil.logInfo(getApplicationContext(),"Battery Level at Start: " + level);
        LogUtil.logInfo(getApplicationContext(),"Time at Start: " + System.currentTimeMillis());
    }

    public void getBatteryLevelAtEnd(View view) {
        String level = String.valueOf(getBatteryLevel());
        LogUtil.logInfo(getApplicationContext(),"Battery Level at End: " + level);
        LogUtil.logInfo(getApplicationContext(),"Time at End: " + System.currentTimeMillis());
    }

    private float getBatteryLevel(){
        Intent batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        float level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);

        return level;
    }
}