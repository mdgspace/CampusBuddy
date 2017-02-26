package in.co.mdg.campusBuddy.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.co.mdg.campusBuddy.R;

/**
 * Created by Harshit Bansal on 2/25/2017.
 */

public class AboutUsFragment extends Fragment {
    private ImageView gitImage,fbImage, blogImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_about_us, container, false);
        initView(view);
        registerListeners();
        return view;
    }

    private void registerListeners() {
        gitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://goo.gl/smpcVZ"));
                startActivity(browser);
            }
        });
        fbImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://goo.gl/6Cznj6"));
                startActivity(browser);
            }
        });
        blogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://mdg.sdslabs.co/"));
                startActivity(browser);
            }
        });
    }

    private void initView(View v) {
        gitImage = (ImageView) v.findViewById(R.id.git_about_us);
        fbImage = (ImageView) v.findViewById(R.id.fb_about_us);
        blogImage = (ImageView) v.findViewById(R.id.blog_about_us);

    }

}
