package com.idee.android.andelatest.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.idee.android.andelatest.R;
import com.idee.android.andelatest.fragments.ProfileDetailsFragment;

public class ProfileDetails extends AppCompatActivity {

    public static final String EXTRA_LIST = "extra_list";
    public static final String EXTRA_POSITION = "extra_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState==null){

            Bundle args = new Bundle();
            args.putParcelableArrayList(EXTRA_LIST, getIntent().getParcelableArrayListExtra("arraylist"));
            args.putInt(EXTRA_POSITION, getIntent().getIntExtra("position",0));

            ProfileDetailsFragment fragment = new ProfileDetailsFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.profile_details,fragment)
                    .commit();
        }

    }

}
