package com.wayyeasy.wayyeasy.Activities;

import static com.wayyeasy.wayyeasy.Adapters.Hospitals.SearchHospital.TAG;
import static com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers.BASE_URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.CustomDialogs.ProgressDialog;
import com.wayyeasy.wayyeasy.Models.user.user_response_model;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.ActivityUserProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity {

    ActivityUserProfileBinding userProfileBinding;
    String profileImage;
    private String id, token, name, age, sex, address, email, city, pinCode;
    ProgressDialog progressDialog;
    SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(userProfileBinding.getRoot());

        userProfileBinding.finishActivity.setOnClickListener(v -> {
            finish();
        });

        preferenceManager = new SharedPreferenceManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            token = preferenceManager.getString(Constants.KEY_MONGO_TOKEN);
            id = preferenceManager.getString(Constants.KEY_MONGO_ID);
        }

        String[] genderSelection = getResources().getStringArray(R.array.gender);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, genderSelection);
        userProfileBinding.genderInput.setAdapter(genderAdapter);

        userProfileBinding.nameInput.setText(preferenceManager.getString(Constants.name));
        userProfileBinding.ageInput.setText(preferenceManager.getString(Constants.age));
        userProfileBinding.emailInput.setText(preferenceManager.getString(Constants.email));
        userProfileBinding.genderInput.setText(preferenceManager.getString(Constants.sex));
        userProfileBinding.cityInput.setText(preferenceManager.getString(Constants.city));
        userProfileBinding.pincodeInput.setText(preferenceManager.getString(Constants.pinCode));
        userProfileBinding.addressInput.setText(preferenceManager.getString(Constants.address));

        if (preferenceManager.getString(Constants.profileImage) != null) {

            String imgUrl = BASE_URL + preferenceManager.getString(Constants.profileImage);

            GlideUrl glideUrl = new GlideUrl(imgUrl,
                    new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build());

            Glide.with(UserProfile.this).load(glideUrl).into(userProfileBinding.profileImage);
        }

        userProfileBinding.cityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                city = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userProfileBinding.pincodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pinCode = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userProfileBinding.emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userProfileBinding.addressInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                address = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userProfileBinding.genderInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sex = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userProfileBinding.ageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                age = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userProfileBinding.nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userProfileBinding.imagePick.setOnClickListener(view -> ImagePicker.with(UserProfile.this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .cropSquare()
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                userProfileBinding.profileImage.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] bytesOfImage = byteArrayOutputStream.toByteArray();
        profileImage = android.util.Base64.encodeToString(bytesOfImage, Base64.DEFAULT);
    }

    public void updateProfile(View view) {
        progressDialog.showDialog();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.name, name);
        map.put(Constants.age, age);
        map.put(Constants.sex, sex);
        map.put(Constants.address, address);
        map.put(Constants.city, city);
        map.put(Constants.pinCode, pinCode);
        map.put(Constants.email, email);
        map.put(Constants.profileImage, profileImage);
        map.put(Constants.KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));
        map.put(Constants.KEY_FIREBASE_USER_ID, preferenceManager.getString(Constants.KEY_FIREBASE_USER_ID));

        Call<user_response_model> call = ApiControllers.getInstance()
                .getApi()
                .updateProfile("Bearer " + token, map);

        call.enqueue(new Callback<user_response_model>() {
            @Override
            public void onResponse(Call<user_response_model> call, Response<user_response_model> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismissDialog();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), "Profile updated successfully.", Toast.LENGTH_SHORT).show();

                    user_response_model data = response.body();

                    preferenceManager.putString(Constants.name, data.getName());
                    preferenceManager.putString(Constants.age, data.getAge());
                    preferenceManager.putString(Constants.email, data.getEmail());
                    preferenceManager.putString(Constants.sex, data.getSex());
                    preferenceManager.putString(Constants.address, data.getAddress());
                    preferenceManager.putString(Constants.pinCode, data.getPinCode());
                    preferenceManager.putString(Constants.city, data.getCity());
                    preferenceManager.putString(Constants.profileImage, data.getProfileImage());
                }
            }

            @Override
            public void onFailure(Call<user_response_model> call, Throwable t) {
                progressDialog.dismissDialog();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onFailure: " + t);
                Toast.makeText(getApplicationContext(), "Failed... " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}