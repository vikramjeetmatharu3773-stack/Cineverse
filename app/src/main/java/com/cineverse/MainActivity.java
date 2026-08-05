package com.cineverse.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "Cineverse";
    private ApiService apiService;
    private Button loadMoviesButton;
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set up the UI
        setContentView(R.layout.activity_main);
        
        loadMoviesButton = findViewById(R.id.load_movies_button);
        statusTextView = findViewById(R.id.status_text);
        
        // Initialize API service
        apiService = RetrofitClient.getClient().create(ApiService.class);
        
        // Set up button click listener
        loadMoviesButton.setOnClickListener(v -> loadMovies());
        
        // Check if we have a deep link
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            handleDeepLink(intent.getData());
        }
        
        Log.d(TAG, "Cineverse app started - phone compatible");
        statusTextView.setText("Cineverse\nMovie Streaming App\n\nAPI: http://10.0.2.2:3000\n\nClick 'Load Movies' to test connection");
    }
    
    private void loadMovies() {
        loadMoviesButton.setEnabled(false);
        statusTextView.setText("Loading movies...");
        
        Call<List<Movie>> call = apiService.getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body();
                    String message = "Loaded " + movies.size() + " movies!\n\n";
                    for (int i = 0; i < Math.min(movies.size(), 3); i++) {
                        message += (i + 1) + ". " + movies.get(i).getTitle() + "\n";
                    }
                    
                    statusTextView.setText(message);
                    Toast.makeText(MainActivity.this, "Successfully loaded movies!", Toast.LENGTH_LONG).show();
                    
                    Log.d(TAG, "Successfully loaded " + movies.size() + " movies");
                } else {
                    statusTextView.setText("Error loading movies: " + response.code());
                    Toast.makeText(MainActivity.this, "Error loading movies", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error loading movies: " + response.code());
                }
                loadMoviesButton.setEnabled(true);
            }
            
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                statusTextView.setText("Network error!\n\nCheck if API server is running at:\nhttp://10.0.2.2:3000");
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error: " + t.getMessage());
                loadMoviesButton.setEnabled(true);
            }
        });
    }
    
    private void handleDeepLink(Uri data) {
        Log.d(TAG, "Deep link received: " + data.toString());
        
        if (data.getScheme().equals("cineverse") && data.getHost().equals("movie")) {
            String movieId = data.getLastPathSegment();
            if (movieId != null) {
                // Open movie player
                Intent playIntent = new Intent(this, MoviePlayerActivity.class);
                playIntent.setData(Uri.parse("https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4"));
                startActivity(playIntent);
            }
        }
    }
}