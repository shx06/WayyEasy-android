package com.wayyeasy.wayyeasy.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayyeasy.wayyeasy.Adapters.PathLabsData;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.Models.PathLabs.pathLabs.path_lab_response_model;
import com.wayyeasy.wayyeasy.databinding.FragmentPathLabsDashboardBinding;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PathLabsDashboard extends Fragment {

    public static final String TAG = "PathLab Dashboard";

    PathLabsData pathLabsData;
    SharedPreferenceManager preferenceManager;
    private String token;

    private FragmentPathLabsDashboardBinding pathLabsDashboard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pathLabsDashboard = FragmentPathLabsDashboardBinding.inflate(inflater, container, false);

        preferenceManager = new SharedPreferenceManager(getContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN))
            token = preferenceManager.getString(Constants.KEY_MONGO_TOKEN);
        pathLabsDashboard.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));

//        fetchAllPathLabs(token);

        return pathLabsDashboard.getRoot();
    }

    private void fetchAllPathLabs(String token) {
        Call<List<path_lab_response_model>> getPathLabs = ApiControllers
                .getInstance()
                .getApi()
                .getAllPathLabs("Bearer " + token);

        getPathLabs.enqueue(new Callback<List<path_lab_response_model>>() {
            @Override
            public void onResponse(Call<List<path_lab_response_model>> call, Response<List<path_lab_response_model>> response) {
                pathLabsDashboard.pathLabsShimmer.stopShimmer();
                pathLabsDashboard.pathLabsShimmer.setVisibility(View.GONE);
                List<path_lab_response_model> dataList = response.body();
                pathLabsData = new PathLabsData(dataList, getActivity().getApplicationContext());
                pathLabsDashboard.recyclerView.setAdapter(pathLabsData);
            }

            @Override
            public void onFailure(Call<List<path_lab_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                pathLabsDashboard.pathLabsShimmer.stopShimmer();
                pathLabsDashboard.pathLabsShimmer.setVisibility(View.GONE);
                pathLabsDashboard.noData.setVisibility(View.VISIBLE);
            }
        });
    }
}