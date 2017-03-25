package in.co.mdg.campusBuddy.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.co.mdg.campusBuddy.R;

public class AboutUsNew extends Fragment {
    ImageView rateImage,shareImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_about_us_new,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        rateImage=(ImageView)v.findViewById(R.id.rate_image);
        shareImage=(ImageView)v.findViewById(R.id.share_image);
    }
}
