package in.co.mdg.campusBuddy.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.co.mdg.campusBuddy.R;

public class AboutUsNew extends Fragment {

    private TextView aboutUsVersion;
    private ImageView rateImage,shareImage,githubImage;

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
        setVersionCode();
        regListeners();
        return view;
    }

    private void setVersionCode(){
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String versionName = pInfo.versionName;
            aboutUsVersion.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initViews(View v) {
        aboutUsVersion=(TextView)v.findViewById(R.id.txt_about_us_version);
        rateImage=(ImageView)v.findViewById(R.id.rate_image);
        shareImage=(ImageView)v.findViewById(R.id.share_image);
        githubImage=(ImageView)v.findViewById(R.id.github_image);
    }

    private void regListeners(){
        rateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + view.getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + view.getContext().getPackageName())));
                }
            }
        });
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                String messageText = getResources().getString(R.string.recommend) +
                        "https://play.google.com/store/apps/details?id=in.co.mdg.campusBuddy&hl=en";
                sendIntent.putExtra(Intent.EXTRA_TEXT, messageText);
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.choose_dialer_text)));
            }
        });
        githubImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.github_link));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}
