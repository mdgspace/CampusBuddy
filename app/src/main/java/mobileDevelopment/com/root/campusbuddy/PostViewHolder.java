package mobileDevelopment.com.root.campusbuddy;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by root on 13/6/15.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView postmessage;
    public TextView postheader;
    public CardView cardview;
    public ImageView fbpostpic;
    public PostViewHolder(View itemView)
    {
        super(itemView);
        postmessage=(TextView)itemView.findViewById(R.id.postmessage);
        postheader=(TextView)itemView.findViewById(R.id.fbpagename);
        fbpostpic=(ImageView)itemView.findViewById(R.id.fbpostpic);
        cardview=(CardView)itemView;
    }
}
