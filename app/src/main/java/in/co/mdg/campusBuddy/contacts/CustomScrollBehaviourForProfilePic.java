package in.co.mdg.campusBuddy.contacts;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.widget.ImageView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import in.co.mdg.campusBuddy.R;

/**
 * Created by Chirag on 18-06-2016.
 */


public class CustomScrollBehaviourForProfilePic extends CoordinatorLayout.Behavior {

    private final static float MIN_AVATAR_PERCENTAGE_SIZE   = 0.6f;

    private final static String TAG = "behavior";
    private final Context mContext;

    private float mStartToolbarPosition;
    private float previousPercent = 1;
    private boolean calledOnce = false;

    public CustomScrollBehaviourForProfilePic(Context context, AttributeSet attrs) {
        mContext = context;
    }



    private float mStartYPosition;
    private float mFinalYPosition;
    private float mStartDiff;
    private float verticalOffset;
    private float offsetY=0;


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if(!calledOnce)
        {
            calledOnce = true;
            initialize(child,dependency);
        }

        verticalOffset = dependency.getY() - mStartYPosition;
        float percentage = (1.0f - (Math.abs(verticalOffset) / mStartYPosition));
        child.setScaleX(scaled(percentage));
        child.setScaleY(scaled(percentage));
        offsetY += (getYForProfilePic(percentage));
        float newY = dependency.getY() - mStartDiff + offsetY;
        child.setY(newY);
        previousPercent=percentage;
        return true;
    }


    private void initialize(View child, View dependency) {
        if (mStartYPosition == 0)
            mStartYPosition = dependency.getY();

        if (mFinalYPosition == 0)
            mFinalYPosition = (dependency.getHeight());

        if (mStartDiff == 0)
            mStartDiff = dependency.getY() - child.getY();
    }

    private float scaled(float percentage) {
        return MIN_AVATAR_PERCENTAGE_SIZE + (percentage*(1f-MIN_AVATAR_PERCENTAGE_SIZE));
    }
    private float getYForProfilePic(float percentComplete)
    {
        return (( mFinalYPosition/2))*(previousPercent-percentComplete);
    }
}
