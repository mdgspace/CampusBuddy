package in.co.mdg.campusBuddy.contacts;


import android.content.Intent;


import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.data_models.Contact;
import in.co.mdg.campusBuddy.contacts.data_models.Department;
import io.realm.Realm;

public class ShowContact extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

//    private static final float MIN_AVATAR_SCALE = 0.5f;
    private int maxScrollSize;
//    private int mActionBarSize;
//    private float previousPercent=1;
    private boolean transparentColorSet=true;
    private ImageView profilePic;
    private String std_code_res_off = "01332 28 "; // std code for roorkee
    private String std_code_bsnl = "01332 "; //std code for roorkee

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(this);
        maxScrollSize = appBarLayout.getTotalScrollRange();

        //To get the height of Action Bar
//        final TypedArray styledAttributes = this.getTheme().obtainStyledAttributes(
//                new int[] { android.R.attr.actionBarSize });
//        mActionBarSize = (int) styledAttributes.getDimension(0, 0);
//        styledAttributes.recycle();

        String name = getIntent().getStringExtra("name");
        String dept = getIntent().getStringExtra("dept");
        Realm realm= Realm.getDefaultInstance();
        Contact contact= realm.where(Department.class).equalTo("name",dept).findFirst().getContacts().where().equalTo("name",name).findFirst();;
        TextView name_text = (TextView) findViewById(R.id.name);
        TextView dept_text = (TextView) findViewById(R.id.dept);
        TextView desg_text = (TextView) findViewById(R.id.desg);
        profilePic = (ImageView) findViewById(R.id.profile_pic);
//        final LinearLayout titleContainer = (LinearLayout) findViewById(R.id.title_container);
        final ImageView profileBackdrop = (ImageView) findViewById(R.id.profile_backdrop);

//        final ViewTreeObserver vto = profilePic.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                    profilePic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                } else {
//                    profilePic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//                AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(dpToPixels(108),dpToPixels(108));
//                Log.d("px 108",dpToPixels(108)+"");
//                params.setMargins(profilePic.getLeft(),-profilePic.getMeasuredHeight()/2,0,0);
//                Log.d("left",profilePic.getLeft()+"");
//                Log.d("top",profilePic.getTop()+","+(mActionBarSize+profilePic.getMeasuredHeight()/2));
//                profilePic.setLayoutParams(params);
//                appBarLayout.getLayoutParams().height = profileBackdrop.getMeasuredHeight()
//                        + mActionBarSize
//                        + profilePic.getMeasuredHeight()/2
//                        + titleContainer.getMeasuredHeight()
//                        + dpToPixels(40);
//                int transY = -(mActionBarSize+(profilePic.getMeasuredHeight()/2));
//                profilePic.setTranslationY(transY);
//                titleContainer.setTranslationY(transY);
//            }
//        });
//        moveProfilePicToCenter();
        Picasso.with(this).load("http://timesofindia.indiatimes.com/photo/48010944.cms").fit().into(profileBackdrop, new Callback() {
            @Override
            public void onSuccess() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
            }

            @Override
            public void onError() {

            }
        });
        name_text.setText(name);
        dept_text.setText(dept);
        if(contact.getDesignation() != null)
        {
            desg_text.setText(contact.getDesignation());
        }
        else
        {
            desg_text.setVisibility(View.GONE);
        }
        if(contact.getProfilePic() != null)
        {
            if(contact.getProfilePic().equals("") || contact.getProfilePic().equals("default.jpg"))
            {
                profilePic.setImageDrawable(
                        ContextCompat.getDrawable(
                                this
                                ,R.drawable.com_facebook_profile_picture_blank_portrait));
            }
            else
            {
                Picasso.with(this)
                        .load("http://people.iitr.ernet.in/facultyphoto/"+contact.getProfilePic())
                        .noFade()
                        .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                        .into(profilePic);
            }
        }
        else
        {
            profilePic.setImageDrawable(
                    ContextCompat.getDrawable(
                            this
                            ,R.drawable.ic_account_circle_black_24dp));
        }
        if(dept.equals("Polymer & Paper Pulp"))
        {
            std_code_res_off = "0132 271 ";
            std_code_bsnl = "0132 ";
        }

        LinearLayout contactOffice = (LinearLayout) findViewById(R.id.contact_office);
        if(contact.getIitr_o()!=null)
        {
            final TextView iitr_o = (TextView) findViewById(R.id.iitr_o);
            iitr_o.setText(std_code_res_off+contact.getIitr_o());
            contactOffice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + iitr_o.getText().toString().replace(" ","")));
                    startActivity(intent);
                }
            });
        }
        else
        {
            findViewById(R.id.divider2).setVisibility(View.GONE);
            contactOffice.setVisibility(View.GONE);
        }

        LinearLayout contactResidence = (LinearLayout) findViewById(R.id.contact_residence);
        if(contact.getIitr_r()!=null)
        {
            final TextView iitr_r = (TextView) findViewById(R.id.iitr_r);
            iitr_r.setText(std_code_res_off+contact.getIitr_r());
            contactResidence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + iitr_r.getText().toString().replace(" ","")));
                    startActivity(intent);
                }
            });
        }
        else
        {
            findViewById(R.id.divider3).setVisibility(View.GONE);
            contactResidence.setVisibility(View.GONE);
        }

        LinearLayout contactBsnl = (LinearLayout) findViewById(R.id.contact_bsnl);
        if(contact.getPhoneBSNL()!=null)
        {
            final TextView phoneBsnl = (TextView) findViewById(R.id.phone_bsnl);
            phoneBsnl.setText(std_code_bsnl+contact.getPhoneBSNL());
            contactBsnl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneBsnl.getText().toString().replace(" ","")));
                    startActivity(intent);
                }
            });
        }
        else
        {
            findViewById(R.id.divider4).setVisibility(View.GONE);
            contactBsnl.setVisibility(View.GONE);
        }

        LinearLayout contactEmail= (LinearLayout) findViewById(R.id.contact_email);
        if(contact.getEmail()!=null)
        {
            final TextView email = (TextView) findViewById(R.id.email);
            email.setText(contact.getEmail()+"@iitr.ac.in");
            contactEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setType("plain/text");
                    intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email.getText().toString()});
                    startActivity(intent);
                }
            });
        }
        else
        {
            findViewById(R.id.divider5).setVisibility(View.GONE);
            contactEmail.setVisibility(View.GONE);
        }

        LinearLayout contactWebsite= (LinearLayout) findViewById(R.id.contact_website);
        if(contact.getWebsite()!=null)
        {
            final TextView website = (TextView) findViewById(R.id.website_link);
            website.setText(contact.getWebsite());
            contactWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website.getText().toString()));
                    startActivity(browserIntent);
                }
            });

        }
        else
        {
            findViewById(R.id.divider6).setVisibility(View.GONE);
            contactWebsite.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (maxScrollSize == 0)
            maxScrollSize = appBarLayout.getTotalScrollRange();
        float percentage = (1.0f - ((float)Math.abs(verticalOffset)) / maxScrollSize);


        if(percentage<0.01f)
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && transparentColorSet) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.brand_primary_dark));
                transparentColorSet=false;
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !transparentColorSet) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                transparentColorSet=true;
            }
        }
//        y += getYForProfilePic(percentage);
//        Log.d("y",y+"");
//        profilePic.setTranslationY(getYForProfilePic(percentage));
//        profilePic.setY(getYForProfilePic(percentage));
//        previousPercent = percentage;
//        profilePic.offsetLeftAndRight((int)(-percentage*10));
//        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
//            mIsAvatarShown = false;
//            profilePic.animate().scaleY(0).scaleX(0).setDuration(200).start();
//        }
//
//        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
//            mIsAvatarShown = true;
//
//            profilePic.animate()
//                    .scaleY(1).scaleX(1)
//                    .start();
//        }
    }

//    private float scaled(float percentage) {
//        return MIN_AVATAR_SCALE + (percentage*(1f-MIN_AVATAR_SCALE));
//    }
//
//    private float getYForProfilePic(float percentComplete)
//    {
//        return ((float)( mActionBarSize/2))*(previousPercent-percentComplete);
//    }
//    private void moveProfilePicToCenter()
//    {
//        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(dpToPixels(108),dpToPixels(108));
//        Log.d("px 108",dpToPixels(108)+"");
//        params.setMargins(profilePic.getLeft(),profilePic.getTop()-(mActionBarSize+profilePic.getMeasuredHeight()/2),0,0);
//        Log.d("left",profilePic.getLeft()+"");
//        Log.d("top",profilePic.getTop()+","+(mActionBarSize+profilePic.getMeasuredHeight()/2));
//        profilePic.setLayoutParams(params);
//    }
//    private int dpToPixels(int dp)
//    {
//        Resources r = this.getResources();
//        return (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                dp,
//                r.getDisplayMetrics()
//        );
//    }
}