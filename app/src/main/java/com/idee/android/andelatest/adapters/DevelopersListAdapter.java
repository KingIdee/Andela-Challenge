package com.idee.android.andelatest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.idee.android.andelatest.R;
import com.idee.android.andelatest.model.DeveloperModel;

import java.util.ArrayList;

public class DevelopersListAdapter extends RecyclerView.Adapter<DevelopersListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<DeveloperModel> developerArrayList = new ArrayList<>();

    public DevelopersListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setMovieList(ArrayList<DeveloperModel> developerArrayList){
        this.developerArrayList = developerArrayList;
        //notifyItemRangeChanged(0,developerArrayList.size());
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DeveloperModel developerModel = developerArrayList.get(position);
        holder.username.setText(developerModel.getUsername());
        Glide.with(mContext)
                .load(developerModel.getImgUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.profileImage);


    }

    @Override
    public int getItemCount() {
        return developerArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView username;
        ImageView profileImage;

        MyViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listListener.adapterOnClick(view,getAdapterPosition());
        }
    }

    public interface ListListener{
        void adapterOnClick(View view, int position);
    }
    ListListener listListener;

    public void setOnItemClick(ListListener listListener){
        this.listListener = listListener;
    }


}
