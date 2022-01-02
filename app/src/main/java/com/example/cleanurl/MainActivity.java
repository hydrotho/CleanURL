package com.example.cleanurl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent receiveIntent = getIntent();
        handleSendText(receiveIntent);
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

        Matcher matcher = Patterns.WEB_URL.matcher(sharedText);
        if (!matcher.find()) {
            shareCleanURL("URL NOT DETECTED");
            finish();
        }
        String shitURL = matcher.group();

        new Thread(() -> {
            try {
                String finalURL = getFinalURL(shitURL);
                String cleanURL = getCleanURL(finalURL);
                shareCleanURL(cleanURL);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                finish();
            }
        }).start();
    }

    private String getFinalURL(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.request().url().toString();
        }
    }

    private String getCleanURL(String url) throws URISyntaxException {
        URI uri = new URI(url);
        return new URI("https", uri.getAuthority(), uri.getPath(), null, null).toString();
    }

    private void shareCleanURL(String string) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, string);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "CleanURL");
        ComponentName[] excludeSelf = {new ComponentName(this, MainActivity.class)};
        shareIntent.putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS, excludeSelf);
        startActivity(shareIntent);
    }
}