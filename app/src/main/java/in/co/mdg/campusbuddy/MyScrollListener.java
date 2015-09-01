package in.co.mdg.campusbuddy;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;

/**
 * Created by root on 13/6/15.
 */
public abstract class MyScrollListener extends RecyclerView.OnScrollListener
{
    public int toolbaroffset=0;
    public int toolbarheight;
    public MyScrollListener(Context context)
    {
        int[] actionbar=new int[]{android.R.attr.actionBarSize};
        TypedArray a=context.obtainStyledAttributes(actionbar);
        toolbarheight=(int)a.getDimension(0,0)+10;
        a.recycle();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView,int dx,int dy)
    {
        super.onScrolled(recyclerView,dx,dy);
        clipToolbarOffset();
        onMoved(toolbaroffset);

        if((toolbaroffset<toolbarheight && dy>0)|| (toolbaroffset>0 && dy<0))
        {
            toolbaroffset+=dy;
        }
    }

    public void clipToolbarOffset()
    {
        if(toolbaroffset>toolbarheight)
        {
            toolbaroffset=toolbarheight;
        }
        else
            if(toolbaroffset<0)
            {
                toolbaroffset=0;
            }
    }

    public abstract void onMoved(int distance);
}
