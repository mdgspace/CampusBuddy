package in.co.mdg.campusBuddy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.util.ArrayList;

import in.co.mdg.campusBuddy.fb.Page;

import static in.co.mdg.campusBuddy.R.drawable.img;
import static in.co.mdg.campusBuddy.R.drawable.thomso;
import static java.security.AccessController.getContext;

/**
 * Created by Harshit Bansal on 3/19/2017.
 */

public class SelectedFbPagesAdapter extends RecyclerView.Adapter<SelectedFbPagesAdapter.MyViewHolder> {

    Activity context;
    String[] pageList;

    public SelectedFbPagesAdapter(Activity context, String[] pageList){
        this.context=context;
        this.pageList=pageList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.fb_selected_page_image);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_fb_page_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            String image = pageList[position];
            Glide.with(context).load(image).into(holder.img);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
/*
        while(!isSelectedPageAttached[0]){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */

    }

    @Override
    public int getItemCount() {
        return pageList.length;
    }


}
