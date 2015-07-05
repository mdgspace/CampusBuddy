package mobileDevelopment.com.root.campusbuddy;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import codetail.graphics.drawables.DrawableHotspotTouch;
import codetail.graphics.drawables.LollipopDrawable;
import codetail.graphics.drawables.LollipopDrawablesCompat;

/**
 * Created by rc on 4/7/15.
 */
public class SampleActivity extends AppCompatActivity {

    private FloatingActionButton mActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionButton = (FloatingActionButton) findViewById(R.id.fbbtn);
        mActionButton.setBackgroundDrawable(getDrawable2(R.drawable.fab_background));
        mActionButton.setClickable(true);// if we don't set it true, ripple will not be played
        mActionButton.setOnTouchListener(
                new DrawableHotspotTouch((LollipopDrawable) mActionButton.getBackground()));
    }

    /**
     * {@link #getDrawable(int)} is already taken by Android API
     * and method is final, so we need to give another name :(
     */
    public Drawable getDrawable2(int id){
        return LollipopDrawablesCompat.getDrawable(getResources(), id, getTheme());
    }
}
