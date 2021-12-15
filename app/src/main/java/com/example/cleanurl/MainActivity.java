package com.example.cleanurl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;

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
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String getFinalURL(String url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
        urlConnection.setInstanceFollowRedirects(false);
        String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36";
        urlConnection.setRequestProperty("User-Agent", UA);
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            String redirectURL = urlConnection.getHeaderField("Location");
            return getFinalURL(redirectURL);
        }

        return url;
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