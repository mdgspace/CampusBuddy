package in.co.mdg.campusBuddy.contacts;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.data_models.Contact;
import in.co.mdg.campusBuddy.contacts.data_models.Department;
import io.realm.Realm;

public class ShowContact extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private int maxScrollSize;
    private ImageView profilePic;
    private ImageView smallProfilePic;
    private String std_code_res_off = "01332 28 "; // std code for roorkee
    private String std_code_bsnl = "01332 "; //std code for roorkee
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.8f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.2f;
    private static final int ALPHA_ANIMATIONS_DURATION = 300;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = false;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private Toolbar toolbar;
    private NestedScrollView nestedScrollView;
    private AppBarLayout mAppBarLayout;
    private TextView name_text;
    private TextView dept_text;
    private TextView desg_text;
    private String name;
    private String dept;

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        initializeVariables();
        setData();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTitle.setVisibility(View.INVISIBLE);
        smallProfilePic.setVisibility(View.INVISIBLE);
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_addtocontacts) {
            addToContacts();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeVariables() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        name_text = (TextView) findViewById(R.id.name);
        dept_text = (TextView) findViewById(R.id.dept);
        desg_text = (TextView) findViewById(R.id.desg);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        mTitleContainer = (LinearLayout) findViewById(R.id.linearlayout_title);
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        smallProfilePic = (ImageView) findViewById(R.id.small_profile_pic);


        maxScrollSize = mAppBarLayout.getTotalScrollRange();
        name = getIntent().getStringExtra("name");
        dept = getIntent().getStringExtra("dept");
        Realm realm = Realm.getDefaultInstance();
        contact = realm
                .where(Department.class)
                .equalTo("name", dept)
                .findFirst()
                .getContacts()
                .where()
                .equalTo("name", name)
                .findFirst();
    }

    private void setData() {
        name_text.setText(name);
        dept_text.setText(dept);
        mTitle.setText(name);

        if (contact.getDesignation() != null)
            desg_text.setText(contact.getDesignation());
        else
            desg_text.setVisibility(View.GONE);

        LoadingImages.loadContactImageForContactView(contact.getProfilePic(),profilePic,smallProfilePic);

        if (dept.equals("Polymer & Paper Pulp")) {
            std_code_res_off = "0132 271 ";
            std_code_bsnl = "0132 ";
        }

        LinearLayout contactOffice = (LinearLayout) findViewById(R.id.contact_office);
        if (contact.getIitr_o() != null) {
            final TextView iitr_o = (TextView) findViewById(R.id.iitr_o);
            iitr_o.setText(std_code_res_off + contact.getIitr_o());
            contactOffice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + iitr_o.getText().toString().replace(" ", "")));
                    startActivity(intent);
                }
            });
            contactOffice.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return copyToClipboard(name, iitr_o.getText().toString().replace(" ", ""));
                }
            });
        } else {
            findViewById(R.id.divider2).setVisibility(View.GONE);
            contactOffice.setVisibility(View.GONE);
        }

        LinearLayout contactResidence = (LinearLayout) findViewById(R.id.contact_residence);
        if (contact.getIitr_r() != null) {
            final TextView iitr_r = (TextView) findViewById(R.id.iitr_r);
            iitr_r.setText(std_code_res_off + contact.getIitr_r());
            contactResidence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + iitr_r.getText().toString().replace(" ", "")));
                    startActivity(intent);
                }
            });
            contactResidence.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return copyToClipboard(name, iitr_r.getText().toString().replace(" ", ""));
                }
            });
        } else {
            findViewById(R.id.divider3).setVisibility(View.GONE);
            contactResidence.setVisibility(View.GONE);
        }

        LinearLayout contactBsnl = (LinearLayout) findViewById(R.id.contact_bsnl);
        if (contact.getPhoneBSNL() != null) {
            final TextView phoneBsnl = (TextView) findViewById(R.id.phone_bsnl);
            if (contact.getPhoneBSNL().startsWith("9") || contact.getPhoneBSNL().startsWith("8")) {
                final TextView bsnlText = (TextView) findViewById(R.id.bsnl_text);
                bsnlText.setText("Mobile");
                phoneBsnl.setText(contact.getPhoneBSNL());
            } else {
                phoneBsnl.setText(std_code_bsnl + contact.getPhoneBSNL());
            }
            contactBsnl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneBsnl.getText().toString().replace(" ", "")));
                    startActivity(intent);
                }
            });
            contactBsnl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return copyToClipboard(name, phoneBsnl.getText().toString().replace(" ", ""));
                }
            });
        } else {
            findViewById(R.id.divider4).setVisibility(View.GONE);
            contactBsnl.setVisibility(View.GONE);
        }

        LinearLayout contactEmail = (LinearLayout) findViewById(R.id.contact_email);
        if (contact.getEmail() != null) {
            final TextView email = (TextView) findViewById(R.id.email);
            email.setText(contact.getEmail() + "@iitr.ac.in");
            contactEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email.getText().toString()));
                    startActivity(intent);
                }
            });
            contactEmail.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return copyToClipboard(name, email.getText().toString());
                }
            });
        } else {
            findViewById(R.id.divider5).setVisibility(View.GONE);
            contactEmail.setVisibility(View.GONE);
        }

        LinearLayout contactWebsite = (LinearLayout) findViewById(R.id.contact_website);
        if (contact.getWebsite() != null) {
            final TextView website = (TextView) findViewById(R.id.website_link);
            website.setText(contact.getWebsite());
            contactWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website.getText().toString()));
                    startActivity(browserIntent);
                }
            });
            contactWebsite.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return copyToClipboard(name, website.getText().toString());
                }
            });

        } else {
            findViewById(R.id.divider6).setVisibility(View.GONE);
            contactWebsite.setVisibility(View.GONE);
        }
    }

    private boolean copyToClipboard(String label, String data) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, data);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean addToContacts() {
        final Intent addContact = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        addContact.putExtra(ContactsContract.Intents.Insert.NAME, name);
        addContact.putExtra(ContactsContract.Intents.Insert.COMPANY, dept);
        if (contact.getDesignation() != null)
            addContact.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, contact.getDesignation());
        if (contact.getIitr_o() != null) {
            addContact.putExtra(ContactsContract.Intents.Insert.PHONE, (std_code_res_off + contact.getIitr_o()).replace(" ", ""));
            addContact.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, "Office");
            addContact.putExtra(ContactsContract.Intents.Insert.PHONE_ISPRIMARY, "True");
        }
        if (contact.getIitr_r() != null) {
            addContact.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, (std_code_res_off + contact.getIitr_r()).replace(" ", ""));
            addContact.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, "Residence");
        }
        if (contact.getPhoneBSNL() != null) {
            if (contact.getPhoneBSNL().startsWith("9") || contact.getPhoneBSNL().startsWith("8")) {
                addContact.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, contact.getPhoneBSNL());
                addContact.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, "Mobile");
            } else {
                addContact.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, std_code_bsnl + contact.getPhoneBSNL());
                addContact.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, "Bsnl Landline");
            }
        }
        if (contact.getEmail() != null)
            addContact.putExtra(ContactsContract.Intents.Insert.EMAIL, contact.getEmail() + "@iitr.ac.in");

        addContact.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        startActivity(addContact);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        if (maxScrollSize == 0)
            maxScrollSize = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScrollSize;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {

        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
//            float modifiedPercent = getModifiedPercent(percentage, PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR);
//            nestedScrollView.setPadding(0, (int) (toolbar.getMeasuredHeight() * modifiedPercent), 0, 0);
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(smallProfilePic, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(smallProfilePic, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }


    private static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

//    private float getModifiedPercent(float percentage, float leastValue) {
//        return (percentage - leastValue) / (1f - leastValue);
//    }

}