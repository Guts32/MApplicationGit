package com.myapp.myapplicationgit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    LoginAdapter loginAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpView();
        setUpViewPagerAdapter();
    }

    private void setUpView(){
        tabLayout = findViewById(R.id.tap_layout);
        viewPager = findViewById(R.id.view_pager);
        loginAdapter = new LoginAdapter(getSupportFragmentManager());
    }

    private void setUpViewPagerAdapter(){
        loginAdapter.addFragment(new Login_Tap_Fragment(), "Iniciar Sesion");
        loginAdapter.addFragment(new SingUp_Tap_Fragment(), "Registrarte");
        viewPager.setAdapter(loginAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}