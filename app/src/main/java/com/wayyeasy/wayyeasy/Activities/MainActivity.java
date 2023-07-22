package com.wayyeasy.wayyeasy.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.wayyeasy.wayyeasy.Fragments.Auth.Login1;
import com.wayyeasy.wayyeasy.R;
import com.wayyeasy.wayyeasy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        getSupportFragmentManager().beginTransaction().add(R.id.layout_replace, new Login1()).commit();
    }
}