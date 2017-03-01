package in.co.mdg.campusBuddy.contacts;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.contacts.data_models.Department;
import io.realm.Realm;

public class ShowDepartmentContacts extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;
//    private int primaryDark;
//    private int primary;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_department_contacts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
//        primary = ContextCompat.getColor(this, R.color.primary);
//        primaryDark = ContextCompat.getColor(this, R.color.primary_dark);
        String deptName = getIntent().getStringExtra("dept_name");
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider, metrics.density));
        ContactsRecyclerAdapter adapter = new ContactsRecyclerAdapter();
        adapter.setListData(3, deptName);
        recyclerView.setAdapter(adapter);
        RecyclerViewFastScroller fastScroller = (RecyclerViewFastScroller) findViewById(R.id.fastscroller);
        fastScroller.setVisibility(View.GONE);
        // fastScroller.setRecyclerView(recyclerView);
        //fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller,R.id.fastscroller_bubble,R.id.fastscroller_handle);

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (firstVisiblePosition == 0) {
                        appBarLayout.setExpanded(true, true);
                    }
                }
            }
        });*/

        collapsingToolbarLayout.setTitle(deptName);
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

//        Realm realm = Realm.getDefaultInstance();
//        Department dept = realm.where(Department.class).equalTo("name", deptName).findFirst();
//        final ImageView deptBackdrop = (ImageView) findViewById(R.id.dept_backdrop);
//
//        BitmapImageViewTarget target = new BitmapImageViewTarget(deptBackdrop) {
//            @Override
//            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                super.onResourceReady(bitmap, anim);
//                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                    public void onGenerated(Palette palette) {
//                        applyPalette(palette);
//                        setEdgeGlowColor(recyclerView, primary);
//                    }
//                });
//            }
//        };
//        LoadingImages.loadDeptImages(dept.getPhoto(), target);


    }

//    private void applyPalette(Palette palette) {
//
//        primary = palette.getMutedColor(primary);
//        primaryDark = palette.getDarkMutedColor(primaryDark);
//        collapsingToolbarLayout.setContentScrimColor(primary);
//        collapsingToolbarLayout.setStatusBarScrimColor(primaryDark);
//        supportStartPostponedEnterTransition();
//    }
//
//    private static void setEdgeGlowColor(final RecyclerView recyclerView, final int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            try {
//                final Class<?> clazz = RecyclerView.class;
//                for (final String name : new String[]{"ensureTopGlow", "ensureBottomGlow"}) {
//                    Method method = clazz.getDeclaredMethod(name);
//                    method.setAccessible(true);
//                    method.invoke(recyclerView);
//                }
//                for (final String name : new String[]{"mTopGlow", "mBottomGlow"}) {
//                    final Field field = clazz.getDeclaredField(name);
//                    field.setAccessible(true);
//                    final Object edge = field.get(recyclerView); // android.support.v4.widget.EdgeEffectCompat
//                    final Field fEdgeEffect = edge.getClass().getDeclaredField("mEdgeEffect");
//                    fEdgeEffect.setAccessible(true);
//                    ((EdgeEffect) fEdgeEffect.get(edge)).setColor(color);
//                }
//            } catch (final Exception ignored) {
//            }
//        }
//    }


}
