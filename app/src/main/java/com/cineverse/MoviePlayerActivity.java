package com.cineverse.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;
import android.widget.MediaController;

public class MoviePlayerActivity extends Activity {
    private static final String TAG = "Cineverse";
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        videoView = new VideoView(this);
        setContentView(videoView);
        
        // Get video URL from intent
        Uri videoUri = getIntent().getData();
        if (videoUri != null) {
            videoView.setVideoURI(videoUri);
            
            // Add media controller for playback controls
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            
            videoView.start();
            Log.d(TAG, "Playing video: " + videoUri.toString());
        } else {
            Log.e(TAG, "No video URI provided");
            finish();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.resume();
        }
    }
}