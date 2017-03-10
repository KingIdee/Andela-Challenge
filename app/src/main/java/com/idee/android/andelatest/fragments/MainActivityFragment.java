package com.idee.android.andelatest.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.idee.android.andelatest.AppController;
import com.idee.android.andelatest.activities.ProfileDetails;
import com.idee.android.andelatest.R;
import com.idee.android.andelatest.adapters.DevelopersListAdapter;
import com.idee.android.andelatest.model.DeveloperModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.idee.android.andelatest.activities.MainActivity.twoPane;

public class MainActivityFragment extends Fragment implements DevelopersListAdapter.ListListener {

    private static final String DEV_LIST = "developers";
    DevelopersListAdapter adapter;
    RecyclerView developerRecyclerList;
    ArrayList<DeveloperModel> developerArrayList = new ArrayList<>();
    final static String URL = "https://api.github.com/search/users?q=language:java%20location:lagos";
    ProgressBar progressBar;
    DividerItemDecoration mDividerItemDecoration;
    TextView errorMessage;
    Button refreshButton;

    public MainActivityFragment() {}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(DEV_LIST, developerArrayList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        errorMessage = (TextView) view.findViewById(R.id.error_message);
        refreshButton = (Button) view.findViewById(R.id.refresh_button);
        developerRecyclerList = (RecyclerView) view.findViewById(R.id.developersList);
        adapter = new DevelopersListAdapter(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mDividerItemDecoration = new DividerItemDecoration(developerRecyclerList.getContext(),
                linearLayoutManager.getOrientation());

        refreshButton.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        developerRecyclerList.setLayoutManager(linearLayoutManager);
        developerRecyclerList.setAdapter(adapter);
        adapter.setOnItemClick(this);
        developerRecyclerList.addItemDecoration(mDividerItemDecoration);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                refreshButton.setVisibility(View.INVISIBLE);
                errorMessage.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                checkConnectionAndMakeRequest();
            }
        });

        if (savedInstanceState != null ){
            Log.d("TAG", "OnSaveInstance Used");
            progressBar.setVisibility(View.INVISIBLE);
            developerArrayList = savedInstanceState.getParcelableArrayList(DEV_LIST);
            adapter.setList(developerArrayList);

        } else {
            checkConnectionAndMakeRequest();
        }

        return view;
    }

    private void checkConnectionAndMakeRequest() {

        if (checkNetworkAvailability()){
            fetchData();
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            refreshButton.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText(R.string.no_internet_connection);
        }
    }

    public void fetchData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject;
                        progressBar.setVisibility(View.INVISIBLE);

                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");

                            for (int i= 0; i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                DeveloperModel developerModel = new DeveloperModel();
                                developerModel.setUserId(object.getString("id"));
                                developerModel.setGithubUrl(object.getString("html_url"));
                                developerModel.setImgUrl(object.getString("avatar_url"));
                                developerModel.setUsername(object.getString("login"));

                                developerArrayList.add(developerModel);
                            }

                            adapter.setList(developerArrayList);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {

                            Log.d("Exception", "Error");
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);

                        errorMessage.setVisibility(View.VISIBLE);
                        refreshButton.setVisibility(View.VISIBLE);

                        Log.e("MainActivityFragment",error.toString());

                        if (error instanceof NoConnectionError){
                            errorMessage.setText(R.string.no_internet_connection);
                        } else if (error instanceof TimeoutError){
                            errorMessage.setText(R.string.time_out_error);
                        } else if (error instanceof NetworkError){
                            errorMessage.setText(R.string.internet_error);
                        }

                    }
                }) {};
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void adapterOnClick(View view, int position) {

        if (twoPane){
            Bundle args = new Bundle();
            args.putInt(ProfileDetails.EXTRA_POSITION, position);
            args.putParcelableArrayList(ProfileDetails.EXTRA_LIST,developerArrayList);

            ProfileDetailsFragment fragment = new ProfileDetailsFragment();
            fragment.setArguments(args);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.user_detail, fragment)
                    .commit();

        } else {
            Intent intent = new Intent(getActivity(), ProfileDetails.class);
            intent.putExtra("position",position);
            intent.putParcelableArrayListExtra("arraylist",developerArrayList);
            startActivity(intent);
        }

    }

    private boolean checkNetworkAvailability() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

}