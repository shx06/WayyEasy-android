package com.wayyeasy.wayyeasy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import com.wayyeasy.wayyeasy.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasy.ComponentFiles.PreferenceManager.SharedPreferenceManager;
import com.wayyeasy.wayyeasy.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding splashScreenBinding;
    SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(splashScreenBinding.getRoot());

        preferenceManager = new SharedPreferenceManager(getApplicationContext());

        splashScreenBinding.brandName.animate().translationY(200).setDuration(1000);
        splashScreenBinding.brandLogo.animate().alpha(1f).setDuration(800).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                splashScreenBinding.brandName1.animate().alpha(1).setDuration(800).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        splashScreenBinding.brandName2.animate().alpha(1f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN))
                                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                else
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}