package mobileDevelopment.com.root.campusbuddy;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by root on 13/6/15.
 */
public class ContactViewHolder extends RecyclerView.ViewHolder {
    public TextView nameT;
    public CardView cardview;
    public ContactViewHolder(View itemView)
    {
        super(itemView);
        nameT=(TextView)itemView.findViewById(R.id.name);
        cardview=(CardView)itemView;
    }
}
