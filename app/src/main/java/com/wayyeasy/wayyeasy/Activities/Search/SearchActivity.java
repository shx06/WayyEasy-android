package com.wayyeasy.wayyeasy.Activities.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.wayyeasy.wayyeasy.Activities.Search.Physicians.PhysicianSearch1;
import com.wayyeasy.wayyeasy.Adapters.Hospitals.SearchHospital;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.Models.Hospitals.search_response_model;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.ActivitySearchBinding;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "Search Activity";

    ActivitySearchBinding searchBinding;
    ProgressDialog progressDialog;
    SearchHospital searchHospital;
    List<search_response_model> dataArrayList = new ArrayList<>();
    private Boolean allowSearch = false;
    String id, token;
    private int limit = 11, page = 1;
    SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(searchBinding.getRoot());

        getSupportFragmentManager().beginTransaction().add(R.id.layout_replace, new PhysicianSearch1()).commit();

        preferenceManager = new SharedPreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            token = preferenceManager.getString(Constants.KEY_MONGO_TOKEN);
            id = preferenceManager.getString(Constants.KEY_MONGO_ID);
        }

        searchHospital = new SearchHospital(dataArrayList, getApplicationContext());
        searchBinding.recyclerView.setAdapter(searchHospital);

        progressDialog = new ProgressDialog(SearchActivity.this);
        searchBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchBinding.recyclerView.setHasFixedSize(true);

        processSearch(limit, page);

        searchBinding.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                page++;
                searchBinding.progressMore.setVisibility(View.VISIBLE);
                processSearch(limit, page);
            }
        });
    }

    private void processSearch(int limit, int page) {

        Call<List<search_response_model>> searchHospitals = ApiControllers
                .getInstance()
                .getApi()
                .searchHospitals("Bearer " + token, "getAll", limit, page);

        searchHospitals.enqueue(new Callback<List<search_response_model>>() {
            @Override
            public void onResponse(Call<List<search_response_model>> call, Response<List<search_response_model>> response) {
                searchBinding.searchShimmer.stopShimmer();
                searchBinding.searchShimmer.setVisibility(View.GONE);
                progressDialog.dismiss();
                allowSearch = true;
                List<search_response_model> data = response.body();
                parseResult(data);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onFailure(Call<List<search_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    private void parseResult(List<search_response_model> data) {
        for (int i = 0; i < data.size(); i++) {
            dataArrayList.add(data.get(i));
        }
        searchBinding.progressMore.setVisibility(View.GONE);
        searchHospital = new SearchHospital(dataArrayList, getApplicationContext());
        searchBinding.recyclerView.setAdapter(searchHospital);
    }
}