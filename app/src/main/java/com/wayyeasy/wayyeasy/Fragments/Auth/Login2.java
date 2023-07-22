package com.wayyeasy.wayyeasy.Fragments.Auth;

import static com.wayyeasy.wayyeasy.Adapters.Hospitals.SearchHospital.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.wayyeasy.wayyeasy.Activities.Dashboard;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.CustomDialogs.ProgressDialog;
import com.wayyeasy.wayyeasy.Models.Hospitals.userVerify.user_verify_response_model;
import com.wayyeasy.wayyeasy.Models.user.user_response_model;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.FragmentLogin2Binding;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login2 extends Fragment {
    FragmentLogin2Binding login2Binding;
    private String otp;
    private int userCount = 0;
    ProgressDialog progressDialog;
    private SharedPreferenceManager preferenceManager;
    private int counter = 3;

    //Firebase mobile auth
    FirebaseAuth firebaseAuth;
    private String otpId;

    public Login2() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        login2Binding = FragmentLogin2Binding.inflate(inflater, container, false);
        login2Binding.heading.setPaintFlags(login2Binding.heading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        login2Binding.editNumber.setPaintFlags(login2Binding.editNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        preferenceManager = new SharedPreferenceManager(getContext());
        progressDialog = new ProgressDialog(getActivity());

//        login2Binding.showNumber.setText("Otp send to " + getArguments().getString(Constants.KEY_AUTHENTICATED_NUMBER));
        login2Binding.editNumber.setOnClickListener(view -> getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.left_to_right_slider, R.anim.right_to_left_slider)
                .replace(R.id.layout_replace, new Login1()).commit());


        //Firebase mobile auth starts here
        FirebaseApp.initializeApp(getContext());
        firebaseAuth = FirebaseAuth.getInstance();
//        initiateAuth();
        userLogin();

        resendOTPTimer();

        login2Binding.resendOtp.setOnClickListener(v -> {
            login2Binding.resendOtp.setEnabled(false);
            initiateAuth();
            resendOTPTimer();
        });

        login2Binding.otpInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().length() == 6) {
//                    otp = charSequence.toString();
//                    userLogin();
//                }


                //Firebase mobile auth
                if (charSequence.toString().length() == 6) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, charSequence.toString());

                    progressDialog.showDialog();

                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                userLogin();
                            } else {
                                progressDialog.dismissDialog();
                                Toast.makeText(getActivity(), "Failed to verify OTP. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return login2Binding.getRoot();
    }

    private void resendOTPTimer() {

        new CountDownTimer(45000, 1000) {
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                login2Binding.remainingTime.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                login2Binding.resendOtp.setEnabled(true);
                login2Binding.remainingTime.setText("00:00:00");
            }
        }.start();
    }

    private void initiateAuth() {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(getArguments().getString(Constants.KEY_AUTHENTICATED_NUMBER))
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(getActivity(), "OTP sent successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onVerificationFailed: Error: " + e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        otpId = s;
                    }
                }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void userLogin() {
        /*
        progressDialog.showDialog();

        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.KEY_AUTHENTICATED_NUMBER, getArguments().getString(Constants.KEY_AUTHENTICATED_NUMBER));
        map.put(Constants.otp, otp);
        Call<user_verify_response_model> call = ApiControllers.getInstance()
                .getApi()
                .login(map);

        call.enqueue(new Callback<user_verify_response_model>() {
            @Override
            public void onResponse(Call<user_verify_response_model> call, Response<user_verify_response_model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getMessage().equals("User created successfully") | response.body().getMessage().equals("User updated successfully")) {
                        onSuccessfulAuthentication(response);
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: " + response);
                    }
                } else {
                    progressDialog.dismissDialog();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<user_verify_response_model> call, Throwable t) {
                progressDialog.dismissDialog();
                Toast.makeText(getContext(), "Failed... " + t.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t);
            }
        });
                 */


        //Firebase mobile auth

        progressDialog.showDialog();

        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.KEY_AUTHENTICATED_NUMBER, getArguments().getString(Constants.KEY_AUTHENTICATED_NUMBER));
        Call<user_verify_response_model> call = ApiControllers.getInstance()
                .getApi()
                .login(map);

        call.enqueue(new Callback<user_verify_response_model>() {
            @Override
            public void onResponse(Call<user_verify_response_model> call, Response<user_verify_response_model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getMessage().equals("User created successfully") | response.body().getMessage().equals("User updated successfully")) {
                        onSuccessfulAuthentication(response);
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismissDialog();
                        Log.d(TAG, "onResponse: " + response);
                    }
                } else {
                    progressDialog.dismissDialog();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<user_verify_response_model> call, Throwable t) {
                progressDialog.dismissDialog();
                Toast.makeText(getContext(), "Failed... " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void onSuccessfulAuthentication(Response<user_verify_response_model> response) {
        progressDialog.dismissDialog();
        Toast.makeText(getActivity(), "Welcome!!!", Toast.LENGTH_SHORT).show();

        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
        preferenceManager.putString(Constants.KEY_AUTHENTICATED_NUMBER, response.body().getData().getNumber());
        preferenceManager.putString(Constants.KEY_MONGO_ID, response.body().getData().get_id());
        preferenceManager.putString(Constants.KEY_MONGO_TOKEN, response.body().getToken());

        Runnable runnable = () -> updateUserFCMToken();

        Thread thread = new Thread(runnable);
        thread.start();

        Intent intent = new Intent(getActivity(), Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    private void updateUserFCMToken() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.
                KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));

        Call<user_response_model> call = ApiControllers.getInstance()
                .getApi()
                .updateProfileToken("Bearer " + preferenceManager.getString(Constants.KEY_MONGO_TOKEN), map);
        try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to update token\nPlease login again.", Toast.LENGTH_SHORT).show();
        }
    }
}