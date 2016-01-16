package in.co.mdg.campusBuddy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
/*
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
*/
/**
 * Created by root on 13/6/15.
 */
public class MyItemClickListener implements RecyclerView.OnItemTouchListener
{
    public OnItemClickListener mListener;

    public interface OnItemClickListener{
         public void OnItemClick(View v,int i, MotionEvent e);
    }
    GestureDetector mgd;

    public MyItemClickListener(Context context,OnItemClickListener listener)
    {
        mListener=listener;
        mgd=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
        public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child=rv.findChildViewUnder(e.getX(), e.getY());
        if(child!=null && mListener!=null && mgd.onTouchEvent(e)){
            mListener.OnItemClick(child,rv.getChildPosition(child),e);
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {


    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
