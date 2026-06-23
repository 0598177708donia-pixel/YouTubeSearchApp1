package com.example.youtube;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.youtube.Adapter.VideoAdapter;
import com.example.youtube.api.RetrofitClient;
import com.example.youtube.api.YouTubeApiService;
import com.example.youtube.model.SearchResponse;
import com.example.youtube.model.VideoItem;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView videosRecyclerView;
    private ProgressBar loadingProgressBar;
    private TextView errorMessageTextView;
    private VideoAdapter videoAdapter;
    private List<VideoItem> videoList;

    private static final String API_KEY = "AIzaSyAEk7F_bbhTFUWxwJXDn5fzxviwCJYk7EY";
    private static final int MAX_RESULTS = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupRecyclerView();
        setupSearchListener();
    }

    private void initializeViews() {
        searchEditText = findViewById(R.id.searchEditText);
        videosRecyclerView = findViewById(R.id.videosRecyclerView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        errorMessageTextView = findViewById(R.id.errorMessageTextView);
    }

    private void setupRecyclerView() {
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(videoList);
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videosRecyclerView.setAdapter(videoAdapter);
    }

    private void setupSearchListener() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();

        if (TextUtils.isEmpty(query)) {
            showErrorMessage("Please enter a search query");
            return;
        }

        searchVideos(query);
    }

    private void searchVideos(String query) {
        showLoading(true);
        hideErrorMessage();

        YouTubeApiService apiService = RetrofitClient.getRetrofitInstance()
                .create(YouTubeApiService.class);

        Call<SearchResponse> call = apiService.searchVideos(
                "snippet",
                "video",
                query,
                MAX_RESULTS,
                API_KEY
        );

        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<VideoItem> results = response.body().getItems();
                    if (results != null && !results.isEmpty()) {
                        videoAdapter.updateList(results);
                    } else {
                        showErrorMessage("No results found");
                    }
                } else {
                    showErrorMessage("Failed to fetch results. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                showLoading(false);
                showErrorMessage("Network error: " + t.getMessage());
            }
        });
    }

    private void showLoading(boolean show) {
        loadingProgressBar.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    private void showErrorMessage(String message) {
        errorMessageTextView.setText(message);
        errorMessageTextView.setVisibility(android.view.View.VISIBLE);
    }

    private void hideErrorMessage() {
        errorMessageTextView.setVisibility(android.view.View.GONE);
    }
}
