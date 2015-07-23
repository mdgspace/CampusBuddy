package mobileDevelopment.com.root.campusbuddy;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by root on 22/7/15.
 */
public class PostViewHolderwithoutfb extends RecyclerView.ViewHolder{

    public TextView postmessage;
    public TextView postheader;
    public CardView cardview;
    public PostViewHolderwithoutfb(View itemView)
    {
        super(itemView);

        postmessage = (TextView) itemView.findViewById(R.id.postmessage);
        postheader = (TextView) itemView.findViewById(R.id.fbpagename);
        cardview = (CardView) itemView;


    }
}
