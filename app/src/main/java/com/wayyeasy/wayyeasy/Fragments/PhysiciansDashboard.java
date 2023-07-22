package com.wayyeasy.wayyeasy.Fragments;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayyeasy.wayyeasy.Adapters.Physicians.PhysiciansData;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.Models.Physicians.physician_response_model;
import com.wayyeasy.wayyeasy.databinding.FragmentPhysiciansDashboardBinding;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhysiciansDashboard extends Fragment {

    private FragmentPhysiciansDashboardBinding doctorsDashboardBinding;
    SharedPreferenceManager preferenceManager;
    List<physician_response_model> dataArrayList = new ArrayList<>();
    PhysiciansData physiciansData;
    private String token;
    private int limit = 15, page = 1;
    private static final String TAG = "Doctors Dashboard";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        doctorsDashboardBinding = FragmentPhysiciansDashboardBinding.inflate(inflater, container, false);

        preferenceManager = new SharedPreferenceManager(getContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN))
            token = preferenceManager.getString(Constants.KEY_MONGO_TOKEN);

        physiciansData = new PhysiciansData(dataArrayList, getActivity(), token);
        doctorsDashboardBinding.recyclerView.setAdapter(physiciansData);
        doctorsDashboardBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchPhysicians(limit, page);

        doctorsDashboardBinding.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                page++;
                doctorsDashboardBinding.progressMore.setVisibility(View.VISIBLE);
                fetchPhysicians(limit, page);
            }
        });

        return doctorsDashboardBinding.getRoot();
    }

    private void fetchPhysicians(int limit, int page) {
        Call<List<physician_response_model>> searchHospitals = ApiControllers
                .getInstance()
                .getApi()
                .getPhysicians("Bearer " + token, "fetchAll", limit, page);

        searchHospitals.enqueue(new Callback<List<physician_response_model>>() {

            @Override
            public void onResponse(Call<List<physician_response_model>> call, Response<List<physician_response_model>> response) {
                doctorsDashboardBinding.doctorsShimmer.stopShimmer();
                doctorsDashboardBinding.doctorsShimmer.setVisibility(View.GONE);
                List<physician_response_model> data = response.body();
                if (data != null && data.size() > 0)
                    parseResult(data, token);
            }

            @Override
            public void onFailure(Call<List<physician_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    private void parseResult(List<physician_response_model> data, String token) {
        for (int i = 0; i < data.size(); i++) {
            dataArrayList.add(data.get(i));
        }

        doctorsDashboardBinding.progressMore.setVisibility(View.GONE);
        physiciansData = new PhysiciansData(dataArrayList, getActivity(), token);
        doctorsDashboardBinding.recyclerView.setAdapter(physiciansData);
    }
}