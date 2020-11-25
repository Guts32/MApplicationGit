package com.myapp.myapplicationgit;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.security.spec.PSSParameterSpec;

public class LoginAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public LoginAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    public Fragment getItem(int Position){
        switch (Position){
            case 0:
                Login_Tap_Fragment login_tap_fragment = new Login_Tap_Fragment();
                return login_tap_fragment;
            case 1:
                SingUp_Tap_Fragment singUp_tap_fragment = new SingUp_Tap_Fragment();
                return singUp_tap_fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
