package com.wayyeasy.wayyeasy.Activities;

import static com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers.BASE_URL;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.wayyeasy.wayyeasy.Activities.Physicians.BookedPhysicians;
import com.wayyeasy.wayyeasy.Activities.Search.SearchActivity;
import com.wayyeasy.wayyeasy.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.ConstantIDs;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.CustomDialogs.ProgressDialog;
import com.wayyeasy.wayyeasy.Fragments.OPDsDashboard;
import com.wayyeasy.wayyeasy.Fragments.PathLabsDashboard;
import com.wayyeasy.wayyeasy.Fragments.PhysiciansDashboard;
import com.wayyeasy.wayyeasy.Models.user.user_response_model;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.ActivityDashboardBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    private ActivityDashboardBinding dashboard;
    ActionBarDrawerToggle toggle;
    private static final String TAG = "Dashboard Activity";
    Dialog dialog;
    ProgressDialog progressDialog;
    ImageView profileImage;
    TextView userName, userAge, userMobile;
    private String token;
    SharedPreferenceManager preferenceManager;

    AppUpdateManager appUpdateManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboard = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(dashboard.getRoot());

        preferenceManager = new SharedPreferenceManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN))
            token = preferenceManager.getString(Constants.KEY_MONGO_TOKEN);

        dashboard.search.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SearchActivity.class)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.theme_color));
        }

        fetchUserProfile(token);

        inAppUpdate();

        //Navigation start
        setSupportActionBar(dashboard.toolbar);

        View header = dashboard.navMenu.getHeaderView(0);
        userName = header.findViewById(R.id.user_name);
        userAge = header.findViewById(R.id.user_age);
        userMobile = header.findViewById(R.id.user_mobile);
        profileImage = header.findViewById(R.id.user_profile_image);
        userMobile.setText(preferenceManager.getString(Constants.KEY_AUTHENTICATED_NUMBER));
        LinearLayout userProfile = header.findViewById(R.id.user_profile);

        userProfile.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UserProfile.class)));
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PhysiciansDashboard()).commit();

        dashboard.bottomNav.setOnItemSelectedListener(item -> {
            Fragment temp = null;
            switch (item.getItemId()) {
                case R.id.doctors:
                    temp = new PhysiciansDashboard();
                    break;
                case R.id.opds:
                    temp = new OPDsDashboard();
                    break;
                case R.id.path_labs:
                    temp = new PathLabsDashboard();
                    break;
//                case R.id.hospitals:
//                    temp = new HospitalsDashboard();
//                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, temp).commit();
            return true;
        });

        toggle = new ActionBarDrawerToggle(this, dashboard.drawer, dashboard.toolbar, R.string.opened, R.string.close);
        dashboard.drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            dashboard.mainManu.version.setText("VERSION  " + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        dashboard.mainManu.doctorsMenuOptions.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        dashboard.mainManu.doctorsMenu.setOnClickListener(view -> {
            int v = (dashboard.mainManu.expandDoctorsNav.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;

            TransitionManager.beginDelayedTransition(dashboard.mainManu.doctorsMenuOptions, new AutoTransition());
            dashboard.mainManu.expandDoctorsNav.setVisibility(v);
        });

        dashboard.mainManu.doctorsBooked.setOnClickListener(view -> {
            dashboard.drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), BookedPhysicians.class);
            intent.putExtra(Constants.fetchPhysicianConsultationType, Constants.pending);
            startActivity(intent);
        });

        dashboard.mainManu.doctorsConsulted.setOnClickListener(view -> {
            dashboard.drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), BookedPhysicians.class);
            intent.putExtra(Constants.fetchPhysicianConsultationType, Constants.finished);
            startActivity(intent);
        });

        dashboard.mainManu.callHistory.setOnClickListener(view -> {
            dashboard.drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(getApplicationContext(), CallHistoryActivity.class));
        });

        dashboard.mainManu.contactUs.setOnClickListener(view -> {
            dashboard.drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(getApplicationContext(), ContactUs.class));
        });

        dashboard.mainManu.asDoctor.setOnClickListener(view -> {
            dashboard.drawer.closeDrawer(GravityCompat.START);
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getResources().getString(R.string.wayyeasy_doctors_app_url))));
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        dashboard.mainManu.privacy.setOnClickListener(view -> {
            dashboard.drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), Privacy.class);
            intent.putExtra(Constants.policiesLink, "file:///android_asset/privacy_policy.html");
            startActivity(intent);
        });

        dashboard.mainManu.terms.setOnClickListener(view -> {
            dashboard.drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), Privacy.class);
            intent.putExtra(Constants.policiesLink, "file:///android_asset/terms_conditions.html");
            startActivity(intent);
        });

        dashboard.mainManu.refund.setOnClickListener(view -> {
            dashboard.drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), Privacy.class);
            intent.putExtra(Constants.policiesLink, "file:///android_asset/refund_policy.html");
            startActivity(intent);
        });

        dashboard.mainManu.share.setOnClickListener(view -> {
            dashboard.drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(Dashboard.this, "Sorry! We do not have share function now as this is our first version.", Toast.LENGTH_SHORT).show();
        });

        //Navigation end
    }

    private void fetchUserProfile(String token) {
        progressDialog.showDialog();
        Call<user_response_model> fetchUser = ApiControllers.getInstance()
                .getApi()
                .fetchUser("Bearer " + token);

        fetchUser.enqueue(new Callback<user_response_model>() {
            @Override
            public void onResponse(Call<user_response_model> call, Response<user_response_model> response) {
                progressDialog.dismissDialog();
                if (response.isSuccessful()) {
                    user_response_model data = response.body();

                    preferenceManager.putString(Constants.KEY_MONGO_ID, data.get_id());
                    preferenceManager.putString(Constants.name, data.getName());
                    preferenceManager.putString(Constants.age, data.getAge());
                    preferenceManager.putString(Constants.email, data.getEmail());
                    preferenceManager.putString(Constants.sex, data.getSex());
                    preferenceManager.putString(Constants.address, data.getAddress());
                    preferenceManager.putString(Constants.pinCode, data.getPinCode());
                    preferenceManager.putString(Constants.city, data.getCity());
                    preferenceManager.putString(Constants.profileImage, data.getProfileImage());

                    if (data.getName() != null && data.getName().length() > 0)
                        userName.setText(data.getName());

                    userAge.setText(data.getAge());

                    if (data.getAge() != null && data.getAge().length() > 0)
                        userAge.setText(data.getAge());

                    if (data.getProfileImage() != null && data.getProfileImage().length() > 0) {
                        String imgUrl = BASE_URL + data.getProfileImage();

                        GlideUrl glideUrl = new GlideUrl(imgUrl,
                                new LazyHeaders.Builder()
                                        .addHeader("Authorization", "Bearer " + token)
                                        .build());

                        Glide.with(Dashboard.this).load(glideUrl).into(profileImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<user_response_model> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                progressDialog.dismissDialog();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantIDs.REQUEST_CODE_UPDATE) {
                Toast.makeText(this, "Update Successful.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_rounded_white_round));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView confirm = dialog.findViewById(R.id.confirm);
        TextView cancel = dialog.findViewById(R.id.cancel);

        confirm.setOnClickListener(view -> finishAffinity());

        cancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    //    --------- Social Media ---------------------------------------------------
    public void openFacebook(View view) {
        try {
            Uri uri = Uri.parse("https://www.facebook.com/WayyEasy");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open url. Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void openLinkedIn(View view) {
        try {
            Uri uri = Uri.parse("https://www.linkedin.com/company/wayyeasy/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open url. Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void openInstagram(View view) {
        try {
            Uri uri = Uri.parse("https://www.instagram.com/wayyeasy/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open url. Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void inAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);

        Task<AppUpdateInfo> task = appUpdateManager.getAppUpdateInfo();

        task.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, Dashboard.this, ConstantIDs.REQUEST_CODE_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        Toast.makeText(Dashboard.this, "Updating app failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "addUpdateFailed: " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        appUpdateManager.registerListener(updateListener);
    }

    InstallStateUpdatedListener updateListener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            popUp();
        }
    };

    private void popUp() {

        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "App update almost completed",
                Snackbar.LENGTH_INDEFINITE
        );

        snackbar.setAction("Reload", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateManager.completeUpdate();
            }
        });

        snackbar.setTextColor(Color.parseColor("#FF0000"));
        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }
}