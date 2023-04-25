package com.cemcoma.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cemcoma.myapplication.R;
import com.cemcoma.myapplication.fragments.dashboardFragment;
import com.cemcoma.myapplication.fragments.marketplaceFragment;
import com.cemcoma.myapplication.fragments.profileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class dashboardActivity extends AppCompatActivity {
    private BottomNavigationView menu;
    private String username;
    private dashboardFragment dashFragment;
    private marketplaceFragment marketFragment;
    private profileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterance_main);
        Intent intent = getIntent();

        dashFragment = new dashboardFragment();
        marketFragment = new marketplaceFragment();
        profileFragment = new profileFragment();

        setFragment(dashFragment);


        username = intent.getStringExtra("username");

        menu = (BottomNavigationView) findViewById(R.id.dashborad_bottomMenu);
        menu.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.profileMenuItem):
                        setFragment(profileFragment);
                        return true;
                    case (R.id.marketplacedMenuItem):
                        setFragment(marketFragment);
                        return true;
                    case (R.id.dashboardMenuItem):
                        setFragment(dashFragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.commit();
    }



}
