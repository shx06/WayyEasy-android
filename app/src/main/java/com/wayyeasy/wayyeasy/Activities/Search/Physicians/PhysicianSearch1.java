package com.wayyeasy.wayyeasy.Activities.Search.Physicians;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wayyeasy.wayyeasy.Activities.Physicians.OutgoingInvitation;
import com.wayyeasy.wayyeasy.Adapters.Physicians.PhysiciansData;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.CustomDialogs.ResponseDialog;
import com.wayyeasy.wayyeasy.Models.Physicians.physician_response_model;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.FragmentPhysicianSearch1Binding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhysicianSearch1 extends Fragment implements PhysiciansTypeAdapter.OnItemClickListener {

    FragmentPhysicianSearch1Binding physicianSearch1;
    SharedPreferenceManager preferenceManager;
    ResponseDialog responseDialog;
    List<physicians_type_response_model> data = new ArrayList<>();
    private String searchQuery = null;
    List<physician_response_model> searchPhysicianList;
    private int limit = 15, page = 1;

    PhysiciansData physiciansData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        physicianSearch1 = FragmentPhysicianSearch1Binding.inflate(inflater, container, false);

        preferenceManager = new SharedPreferenceManager(getActivity());
        responseDialog = new ResponseDialog();

        String[] specialityList = getResources().getStringArray(R.array.speciality);

        for (int i = 0; i < specialityList.length; i++) {
            data.add(new physicians_type_response_model(specialityList[i]));
        }

        PhysiciansTypeAdapter physiciansTypeAdapter = new PhysiciansTypeAdapter(data, this);
        physicianSearch1.physicianTypeList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        physicianSearch1.physicianTypeList.setAdapter(physiciansTypeAdapter);

        physicianSearch1.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                searchQuery = s.toString();

                if (!(searchQuery != null && searchQuery.length() > 0)) {
                    physicianSearch1.physicianTypeList.setVisibility(View.VISIBLE);
                    physicianSearch1.scrollView.setVisibility(View.GONE);
                    physicianSearch1.emptyListSection.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        physicianSearch1.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                page++;
                physicianSearch1.progressMore.setVisibility(View.VISIBLE);
                fetchSearchedPhysicianList(searchQuery, limit, page);
            }
        });

        physicianSearch1.searchBtn.setOnClickListener(v -> {

            if (searchQuery != null && searchQuery.length() > 0) {
                physicianSearch1.physicianTypeList.setVisibility(View.GONE);
                physicianSearch1.scrollView.setVisibility(View.VISIBLE);
                fetchSearchedPhysicianList(searchQuery, limit, page);
            } else {
                physicianSearch1.physicianTypeList.setVisibility(View.VISIBLE);
                physicianSearch1.scrollView.setVisibility(View.GONE);
            }

        });

        return physicianSearch1.getRoot();
    }

    private void fetchSearchedPhysicianList(String searchQuery, int limit, int page) {

        physicianSearch1.doctorsShimmer.setVisibility(View.VISIBLE);

        Call<List<physician_response_model>> searchPhysicians = ApiControllers
                .getInstance()
                .getApi()
                .getPhysiciansBySearch("Bearer " + preferenceManager.getString(Constants.KEY_MONGO_TOKEN), searchQuery, limit, page);

        searchPhysicians.enqueue(new Callback<List<physician_response_model>>() {
            @Override
            public void onResponse(Call<List<physician_response_model>> call, Response<List<physician_response_model>> response) {
                physicianSearch1.doctorsShimmer.stopShimmer();
                physicianSearch1.doctorsShimmer.setVisibility(View.GONE);
                searchPhysicianList = response.body();
                if (searchPhysicians != null && searchPhysicianList.size() > 0) {
                    physicianSearch1.emptyListSection.setVisibility(View.GONE);
                    physicianSearch1.progressMore.setVisibility(View.GONE);
                    physicianSearch1.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    physiciansData = new PhysiciansData(searchPhysicianList, getActivity(), preferenceManager.getString(Constants.KEY_MONGO_TOKEN));
                    physiciansData.notifyDataSetChanged();
                    physicianSearch1.recyclerView.setAdapter(physiciansData);
                } else
                    physicianSearch1.emptyListSection.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<physician_response_model>> call, Throwable t) {
                Log.d(OutgoingInvitation.TAG, "onFailure: " + t.getMessage());
                responseDialog.showDialog(getActivity(), getResources().getDrawable(R.drawable.error), "Error", getResources().getColor(R.color.theme_color_orange_20), getResources().getColor(R.color.theme_color_orange), t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(physicians_type_response_model item) {

        PhysiciansSearch2 physiciansSearch2 = new PhysiciansSearch2();
        Bundle args = new Bundle();
        args.putString(Constants.physicianSearchType, item.getType());
        physiciansSearch2.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.layout_replace, physiciansSearch2).commit();
    }
}