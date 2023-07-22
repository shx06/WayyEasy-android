package com.wayyeasy.wayyeasy.Fragments.Auth;

import static com.wayyeasy.wayyeasy.Fragments.PathLabsDashboard.TAG;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wayyeasy.wayyeasy.Activities.Physicians.PhysicianDetails;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.CustomDialogs.ProgressDialog;
import com.wayyeasy.wayyeasy.CustomDialogs.ResponseDialog;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.FragmentLogin1Binding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login1 extends Fragment {
    private FragmentLogin1Binding login1Binding;
    ProgressDialog progressDialog;

    ResponseDialog dialog;

    public Login1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        login1Binding = FragmentLogin1Binding.inflate(inflater, container, false);

        login1Binding.heading.setPaintFlags(login1Binding.heading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        dialog = new ResponseDialog();

        if (Constants.mobileEntered != null) {
            login1Binding.mobileInput.setText(Constants.mobileEntered);
            checkMobileNumber();
        }

        progressDialog = new ProgressDialog(getActivity());
        login1Binding.mobileInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 9) {
                    checkMobileNumber();
                    checkMobileNumber();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        login1Binding.getMessage.setOnClickListener(view -> {
            /*

            progressDialog.showDialog();

            HashMap<String, String> map = new HashMap<>();
            map.put(Constants.KEY_AUTHENTICATED_NUMBER, login1Binding.mobileInput.getText().toString());

            Call<Void> call = ApiControllers.getInstance()
                    .getApi()
                    .generateOTP(map);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    progressDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        Constants.mobileEntered = login1Binding.mobileInput.getText().toString();
                        Login2 login = new Login2();
                        Bundle args = new Bundle();
                        args.putString(Constants.KEY_AUTHENTICATED_NUMBER, login1Binding.mobileInput.getText().toString());
                        login.setArguments(args);
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.left_to_right_slider, R.anim.right_to_left_slider)
                                .replace(R.id.layout_replace, login).commit();
                    } else {
                        Toast.makeText(getActivity(), "Response: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    progressDialog.dismissDialog();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    dialog.showDialog(getActivity(), getResources().getDrawable(R.drawable.error), "Error", getResources().getColor(R.color.theme_color_orange_20), getResources().getColor(R.color.theme_color_orange), t.getMessage());
                }
            });
                        */


            login1Binding.ccp.registerCarrierNumberEditText(login1Binding.mobileInput);
            Constants.mobileEntered = login1Binding.ccp.getFullNumberWithPlus().replace(" ", "");

            Login2 login = new Login2();
            Bundle args = new Bundle();
            args.putString(Constants.KEY_AUTHENTICATED_NUMBER, Constants.mobileEntered);
            login.setArguments(args);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.left_to_right_slider, R.anim.right_to_left_slider)
                    .replace(R.id.layout_replace, login).commit();
        });

        return login1Binding.getRoot();
    }

    private void checkMobileNumber() {
        login1Binding.getMessage.setEnabled(true);
    }
}