package in.co.mdg.campusBuddy.contacts;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import in.co.mdg.campusBuddy.MyRecyclerAdapter;
import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.data_models.Department;
import io.realm.Realm;

public class ShowDepartmentContacts extends AppCompatActivity{

    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_department_contacts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        String deptName = getIntent().getStringExtra("dept_name");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,R.drawable.divider,metrics.density));
        MyRecyclerAdapter adapter = new MyRecyclerAdapter();
        adapter.setListData(3,deptName);
        recyclerView.setAdapter(adapter);
        RecyclerViewFastScroller fastScroller = (RecyclerViewFastScroller) findViewById(R.id.fastscroller);
        fastScroller.setVisibility(View.GONE);
//        fastScroller.setRecyclerView(recyclerView);
//        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller,R.id.fastscroller_bubble,R.id.fastscroller_handle);

        collapsingToolbarLayout.setTitle(deptName);
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this,android.R.color.transparent));

        Realm realm = Realm.getDefaultInstance();
        Department dept = realm.where(Department.class).equalTo("name",deptName).findFirst();
        final ImageView deptBackdrop = (ImageView)findViewById(R.id.dept_backdrop);
        String deptPhoto;
        if(dept.getPhoto() != null)
        {
            if(dept.getPhoto().length()>4)
                deptPhoto = dept.getPhoto();
            else
                deptPhoto = "http://www.iitr.ac.in/departments/" + dept.getPhoto() + "/assets/images/top1.jpg";
            Picasso.with(this)
                    .load(deptPhoto)
                    .fit()
                    .into(deptBackdrop, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) deptBackdrop.getDrawable()).getBitmap();
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette palette) {
                                    applyPalette(palette);
                                }
                            });
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }


    }
    private void applyPalette(Palette palette) {
        int primaryDark = ContextCompat.getColor(this,R.color.primary_dark);
        int primary = ContextCompat.getColor(this,R.color.primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        supportStartPostponedEnterTransition();
    }

}
