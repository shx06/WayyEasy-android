package com.wayyeasy.wayyeasy.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.databinding.FragmentOPDsDashboardBinding;

public class OPDsDashboard extends Fragment {

    FragmentOPDsDashboardBinding opDsDashboardBinding;

    public OPDsDashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        opDsDashboardBinding = FragmentOPDsDashboardBinding.inflate(inflater, container, false);

        return opDsDashboardBinding.getRoot();
    }
}