package in.co.mdg.campusBuddy.contacts;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
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

import static in.co.mdg.campusBuddy.contacts.ContactsRecyclerAdapter.ENGLISH;
import static in.co.mdg.campusBuddy.contacts.ContactsRecyclerAdapter.HINDI;

public class ShowContact extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private int maxScrollSize;
    private ImageView profilePic;
    private ImageView smallProfilePic;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.8f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.2f;
    private static final int ALPHA_ANIMATIONS_DURATION = 300;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = false;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private Toolbar toolbar;
    //    private NestedScrollView nestedScrollView;
    private AppBarLayout mAppBarLayout;
    private TextView name_text;
    private TextView dept_text;
    private TextView desg_text;
    private String name;
    private String group;
    private String std_code_res_off;
    private String std_code_bsnl;

    private Contact contact;
    private Department department;
    private int lang;

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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
//        nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        mTitleContainer = (LinearLayout) findViewById(R.id.linearlayout_title);
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        smallProfilePic = (ImageView) findViewById(R.id.small_profile_pic);


        maxScrollSize = mAppBarLayout.getTotalScrollRange();
        name = getIntent().getStringExtra("name");
        String dept = getIntent().getStringExtra("dept");
        group = getIntent().getStringExtra("group");
        lang = getIntent().getIntExtra("lang",ENGLISH);
        Realm realm = Realm.getDefaultInstance();
        department = realm
                .where(Department.class)
                .equalTo("name", dept)
                .findFirst();
        contact = department
                .getContacts()
                .where()
                .equalTo("name", name)
                .findFirst();
    }

    private void setData() {
        if (lang == HINDI && contact.getNameHindi() != null) {
            name_text.setText(contact.getNameHindi());
            mTitle.setText(contact.getNameHindi());
        } else {
            name_text.setText(contact.getName());
            mTitle.setText(contact.getName());
        }
        if (lang == HINDI && department.getNameHindi() != null) {
            dept_text.setText(department.getNameHindi());
        } else {
            dept_text.setText(department.getName());
        }


        if (contact.getDesg() != null) {
            if (lang == HINDI && contact.getDesgHindi() != null) {
                desg_text.setText(contact.getDesgHindi());
            } else {
                desg_text.setText(contact.getDesg());
            }
        } else {
            desg_text.setVisibility(View.GONE);
        }

//        LoadingImages.loadContactImageForContactView(contact.getProfilePic(), profilePic, smallProfilePic);


        if (group.equals("Saharanpur Campus")) {
            std_code_res_off = getResources().getString(R.string.std_code_res_off_shrnpr);
            std_code_bsnl = getResources().getString(R.string.std_code_bsnl_shrnpr);
        } else {
            std_code_res_off = getResources().getString(R.string.std_code_res_off_rk);
            std_code_bsnl = getResources().getString(R.string.std_code_bsnl_rk);
        }

        LinearLayout contactOffice = (LinearLayout) findViewById(R.id.contact_office);
        if (contact.getIitr_o().size() > 0) {
            final TextView iitr_o = (TextView) findViewById(R.id.iitr_o);
            iitr_o.setText(String.format("%s%s", std_code_res_off, contact.getIitr_o().get(0).getNumber()));
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
        if (contact.getIitr_r().size() > 0) {
            final TextView iitr_r = (TextView) findViewById(R.id.iitr_r);
            iitr_r.setText(String.format("%s%s", std_code_res_off, contact.getIitr_r().get(0).getNumber()));
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
        if (contact.getPhoneBSNL().size() > 0) {
            final TextView phoneBsnl = (TextView) findViewById(R.id.phone_bsnl);
            phoneBsnl.setText(String.format("%s%s", std_code_bsnl, contact.getPhoneBSNL().get(0).getNumber()));
//            }
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
        if (contact.getEmail().size() > 0) {
            final TextView email = (TextView) findViewById(R.id.email);
            String emailId = contact.getEmail().get(0).getNumber();
            if (!emailId.contains("@")) {
                emailId = emailId.concat("@iitr.ac.in");
            }
            email.setText(emailId);
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

        LinearLayout contactMobile = (LinearLayout) findViewById(R.id.contact_mobile);
        if (contact.getMobile().size() > 0) {
            final TextView mobile = (TextView) findViewById(R.id.mobile);
            mobile.setText(contact.getMobile().get(0).getNumber());
            contactMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile.getText().toString().replace(" ", "")));
                    startActivity(intent);
                }
            });
            contactMobile.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return copyToClipboard(name, mobile.getText().toString());
                }
            });

        } else {
            findViewById(R.id.divider6).setVisibility(View.GONE);
            contactMobile.setVisibility(View.GONE);
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
            findViewById(R.id.divider7).setVisibility(View.GONE);
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
        addContact.putExtra(ContactsContract.Intents.Insert.NAME, name_text.getText().toString());
        addContact.putExtra(ContactsContract.Intents.Insert.COMPANY, dept_text.getText().toString());
        if (contact.getDesg() != null)
            addContact.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, desg_text.getText().toString());
        if (contact.getIitr_o().size() > 0) {
            addContact.putExtra(ContactsContract.Intents.Insert.PHONE, (std_code_res_off + contact.getIitr_o().get(0).getNumber()).replace(" ", ""));
            addContact.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, "Office");
            addContact.putExtra(ContactsContract.Intents.Insert.PHONE_ISPRIMARY, "True");
        }
        if (contact.getIitr_r().size() > 0) {
            addContact.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, (std_code_res_off + contact.getIitr_r().get(0).getNumber()).replace(" ", ""));
            addContact.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, "Residence");
        }
        if (contact.getPhoneBSNL().size() > 0) {
            addContact.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, std_code_bsnl + contact.getPhoneBSNL().get(0).getNumber());
            addContact.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, "Bsnl Landline");
        }
        if (contact.getMobile().size() > 0) {
            addContact.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, contact.getMobile().get(0).getNumber());
            addContact.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, "Mobile");
        }
        if (contact.getEmail().size() > 0) {
            addContact.putExtra(ContactsContract.Intents.Insert.EMAIL, contact.getEmail().get(0).getNumber() + "@iitr.ac.in");
            addContact.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, "Office");
        }

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