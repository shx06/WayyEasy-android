package com.wayyeasy.wayyeasy.Activities.Search.Physicians;

import static com.wayyeasy.wayyeasy.Activities.Physicians.OutgoingInvitation.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wayyeasy.wayyeasy.Adapters.Physicians.PhysiciansData;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.CustomDialogs.ProgressDialog;
import com.wayyeasy.wayyeasy.CustomDialogs.ResponseDialog;
import com.wayyeasy.wayyeasy.Models.Physicians.physician_response_model;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.FragmentPhysiciansSearch2Binding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhysiciansSearch2 extends Fragment {

    FragmentPhysiciansSearch2Binding physiciansSearch2;
    ProgressDialog progressDialog;
    SharedPreferenceManager preferenceManager;
    private int limit = 15, page = 1;

    ResponseDialog responseDialog;

    private String physicianDataType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        physiciansSearch2 = FragmentPhysiciansSearch2Binding.inflate(inflater, container, false);

        preferenceManager = new SharedPreferenceManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        responseDialog = new ResponseDialog();

        physicianDataType = getArguments().getString(Constants.physicianSearchType);

        physiciansSearch2.closeActivity.setOnClickListener(v -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.layout_replace, new PhysicianSearch1()).commit();
        });

        fetchPhysiciansList(physicianDataType, limit, page);

        physiciansSearch2.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                page++;
                physiciansSearch2.progressMore.setVisibility(View.VISIBLE);
                fetchPhysiciansList(physicianDataType, limit, page);
            }
        });

        return physiciansSearch2.getRoot();
    }

    private void fetchPhysiciansList(String dataType, int limit, int page) {
        Call<List<physician_response_model>> searchHospitals = ApiControllers
                .getInstance()
                .getApi()
                .getPhysicians("Bearer " + preferenceManager.getString(Constants.KEY_MONGO_TOKEN), dataType, limit, page);

        searchHospitals.enqueue(new Callback<List<physician_response_model>>() {

            @Override
            public void onResponse(Call<List<physician_response_model>> call, Response<List<physician_response_model>> response) {
                physiciansSearch2.doctorsShimmer.stopShimmer();
                physiciansSearch2.doctorsShimmer.setVisibility(View.GONE);
                List<physician_response_model> data = response.body();
                if (data != null && data.size() > 0) {
                    physiciansSearch2.progressMore.setVisibility(View.GONE);
                    physiciansSearch2.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    PhysiciansData physiciansData = new PhysiciansData(data, getActivity(), preferenceManager.getString(Constants.KEY_MONGO_TOKEN));
                    physiciansSearch2.recyclerView.setAdapter(physiciansData);
                } else
                    physiciansSearch2.emptyListSection.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<physician_response_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}