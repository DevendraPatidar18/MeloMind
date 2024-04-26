package com.example.melomind;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebViewClient;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class web extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebView webView = findViewById(R.id.webView);
        Toast.makeText(this, "invoked", Toast.LENGTH_SHORT).show();
        webView.loadUrl("https://gaana.com");


    }
}