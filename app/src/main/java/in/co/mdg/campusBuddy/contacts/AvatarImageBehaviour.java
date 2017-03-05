package in.co.mdg.campusBuddy.contacts;

/**
 * Created by Chirag on 22-06-2016.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import de.hdodenhof.circleimageview.CircleImageView;
import in.co.mdg.campusBuddy.R;


class AvatarImageBehaviour extends CoordinatorLayout.Behavior<CircleImageView> {


    private Context mContext;

    private float mCustomFinalYPosition;
    private float mCustomFinalXPosition;
    private float mCustomFinalHeight;
    private int mStartXPosition;
    private float mStartToolbarPosition;
    private int mStartYPosition;
    private int mFinalYPosition;
    private int mStartHeight;
    private int mFinalXPosition;
    private float mChangeBehaviorPoint;

    public AvatarImageBehaviour(Context context, AttributeSet attrs) {
        mContext = context;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior);
            mCustomFinalYPosition = a.getDimension(R.styleable.AvatarImageBehavior_finalYPosition, 0);
            mCustomFinalXPosition = a.getDimension(R.styleable.AvatarImageBehavior_finalXPosition, 0);
            mCustomFinalHeight = a.getDimension(R.styleable.AvatarImageBehavior_finalHeight, 0);
            a.recycle();
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
        maybeInitProperties(child, dependency);

        final int maxScrollDistance = (int) (mStartToolbarPosition);
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

        if (expandedPercentageFactor < mChangeBehaviorPoint) {
            float heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint;

            float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
                    * heightFactor) + (child.getHeight()/2);
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (child.getHeight()/2);

            child.setX(mStartXPosition - distanceXToSubtract);
            child.setY(mStartYPosition - distanceYToSubtract);

            float heightToSubtract = ((mStartHeight - mCustomFinalHeight) * heightFactor);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (mStartHeight - heightToSubtract);
            lp.height = (int) (mStartHeight - heightToSubtract);
            child.setLayoutParams(lp);
        } else {
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (mStartHeight/2);

            child.setX(mStartXPosition - child.getWidth()/2);
            child.setY(mStartYPosition - distanceYToSubtract);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = mStartHeight;
            lp.height = mStartHeight;
            child.setLayoutParams(lp);
        }
        return true;
    }

    private void maybeInitProperties(CircleImageView child, View dependency) {
        if (mStartYPosition == 0)
            mStartYPosition = (int) (dependency.getY());

        if (mFinalYPosition == 0)
        {
            if(mCustomFinalYPosition != 0)
                mFinalYPosition = (int) mCustomFinalYPosition + (dependency.getHeight() /2);
            else
                mFinalYPosition = (dependency.getHeight() /2);
        }


        if (mStartHeight == 0)
            mStartHeight = child.getHeight();

        if (mStartXPosition == 0)
            mStartXPosition = (int) child.getX() + (child.getWidth() / 2);

        if (mFinalXPosition == 0)
        {
            if(mCustomFinalXPosition != 0)
                mFinalXPosition = (int) (mCustomFinalXPosition + ( mCustomFinalHeight / 2));
            else
                mFinalXPosition = mContext
                        .getResources()
                        .getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material)
                        + ((int) mCustomFinalHeight / 2);
        }

        if (mStartToolbarPosition == 0)
            mStartToolbarPosition = dependency.getY();

        if (mChangeBehaviorPoint == 0) {
            mChangeBehaviorPoint = (child.getHeight() - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition));
        }
    }

//    public int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
//
//        if (resourceId > 0) {
//            result = mContext.getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
}

