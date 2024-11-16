package com.example.microreader.Tools;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.microreader.MainActivity;
import com.example.microreader.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LlmConnection extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();

    public void togetherAi(MainActivity context) {
        new Thread(() -> {
            // Call the synchronous post method
            String response = post();

            // Update the TextView on the main thread
            context.runOnUiThread(() -> {
                TextView helloTextView = context.findViewById(R.id.textView);

                context.tts.speak(parseContent(response));

            });
        }).start();
    }


    public String post() {
        String apiUrl = "https://api.together.xyz/v1/chat/completions";
        String apiKey = "your_api_key"; // Replace with your actual API key

        String json = "{"
                + "\"model\": \"meta-llama/Meta-Llama-3.1-405B-Instruct-Turbo\","
                + "\"messages\": ["
                + "    {\"role\": \"user\", \"content\": \"Расскажи анекдот\"}"
                + "]"
                + "}";

        RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                json );

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        Call call = client.newCall(request);
        try (Response response = call.execute()) { // Synchronous call
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string(); // Return the response body as a string
            } else {
                throw new IOException("Request failed with code: " + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String parseContent(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray choices = jsonObject.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject choice = choices.getJSONObject(0);
                JSONObject message = choice.getJSONObject("message");
                return message.getString("content");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if parsing fails

    }
}
