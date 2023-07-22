package com.wayyeasy.wayyeasy.Fragments.Hospitals;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayyeasy.wayyeasy.Adapters.Hospitals.HospitalContactData;
import com.wayyeasy.wayyeasy.databinding.FragmentHospitalContactBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class HospitalContact extends BottomSheetDialogFragment {

    private static final String TAG = "Hospital Contact";
    FragmentHospitalContactBinding contactBinding;

    String contactOptions[];

    public HospitalContact(String[] contactOptions) {
        this.contactOptions = contactOptions;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactBinding = FragmentHospitalContactBinding.inflate(inflater, container, false);

        contactBinding.hospitalContacts.setHasFixedSize(true);
        contactBinding.hospitalContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        HospitalContactData hospitalContactData = new HospitalContactData(contactOptions, getContext());
        contactBinding.hospitalContacts.setAdapter(hospitalContactData);

        return contactBinding.getRoot();
    }
}