package com.cineverse.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "Cineverse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView textView = new TextView(this);
        textView.setText("Cineverse\nMovie Streaming App\n\nAPI: http://10.0.2.2:3000\n\nWorks on Your Phone!");
        textView.setTextSize(18);
        setContentView(textView);
        
        Log.d(TAG, "Cineverse app started - phone compatible");
    }
}