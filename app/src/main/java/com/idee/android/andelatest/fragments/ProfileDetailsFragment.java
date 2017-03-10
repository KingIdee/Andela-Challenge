package com.idee.android.andelatest.fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idee.android.andelatest.activities.ProfileDetails;
import com.idee.android.andelatest.R;
import com.idee.android.andelatest.model.DeveloperModel;

import java.util.ArrayList;

public class ProfileDetailsFragment extends Fragment {

    public ProfileDetailsFragment() {
        setHasOptionsMenu(true);
    }

    TextView githubUrl, username;
    ImageView userImage;
    ArrayList<DeveloperModel> list = new ArrayList<>();
    int position;
    ShareActionProvider mShareActionProvider;
    String extra= "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile_details, container, false);


        githubUrl = (TextView) view.findViewById(R.id.githubUrl);
        username = (TextView) view.findViewById(R.id.username);
        userImage = (ImageView) view.findViewById(R.id.userImage);

        Bundle args = getArguments();

        if (args!=null){
            Log.d("String",String.valueOf(args));
            list = args.getParcelableArrayList(ProfileDetails.EXTRA_LIST);
            position = args.getInt(ProfileDetails.EXTRA_POSITION);
        }

        if (!list.isEmpty()){

            DeveloperModel developerModel = list.get(position);

            githubUrl.setText(developerModel.getGithubUrl());
            username.setText(developerModel.getUsername());

            Glide.with(getActivity())
                    .load(developerModel.getImgUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(userImage);

            extra=("Check out this awesome developer ")
                    .concat(developerModel.getUsername())
                    .concat(", ")
                    .concat(developerModel.getGithubUrl())
                    .concat(".");

        }

        final Intent i;
        i = new Intent(Intent.ACTION_VIEW);

        if (githubUrl.getText()!=null) {
            githubUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (githubUrl.getText() != null)
                        i.setData(Uri.parse(githubUrl.getText().toString().trim()));
                    startActivity(i);
                }
            });
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_profile_details, menu);
        MenuItem menuItem = menu.findItem(R.id.share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mShareActionProvider.setShareIntent(shareIntent());

    }

    private Intent shareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, extra );
        return shareIntent;
    }

}
