package com.idee.android.andelatest.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.idee.android.andelatest.R;
import com.idee.android.andelatest.fragments.ProfileDetailsFragment;

public class MainActivity extends AppCompatActivity {

    public static Boolean twoPane = false;
    ProfileDetailsFragment profileDetailsFragment;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState,"profileDetailsFragment", profileDetailsFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.user_detail)!=null){
            twoPane=true;

            if (savedInstanceState==null){

                 profileDetailsFragment = new ProfileDetailsFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.user_detail, profileDetailsFragment )
                        .commit();
            } else {
                profileDetailsFragment = (ProfileDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState,"profileDetailsFFragment");
            }

        } else {

            if (savedInstanceState!=null) {
                profileDetailsFragment = (ProfileDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "profileDetailsFFragment");
                Log.d("TAG", "Saved Instance State");
            }
            twoPane=false;
        }

    }

}