package mobileDevelopment.com.root.campusbuddy;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author Akshay
 * @version 1.0.0
 * @since 25-Jul-15
 */
public class FBFeedAdapter extends ArrayAdapter<Post> {

    ArrayList<Post> arrayList;
    Context context;
    private static LayoutInflater inflater;

    public FBFeedAdapter(Context context, int resource, ArrayList<Post> arrayList) {
        super(context, resource,arrayList);
        this.arrayList = arrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.card_viewfb, parent, false);
            holder = new Holder();
            holder.postmessage = (DynamicHeightTextView) convertView.findViewById(R.id.postmessage);
            holder.postheader = (TextView) convertView.findViewById(R.id.fbpagename);
            holder.fbpostpic = (DynamicHeightImageView) convertView.findViewById(R.id.fbpostpic);
            holder.fbpostpicicon = (DynamicHeightImageView) convertView.findViewById(R.id.fbpostpicicon);

            convertView.setTag(holder);
        } else {
          holder = (Holder) convertView.getTag();
        }

        Post post = arrayList.get(position);

        holder.postmessage.setText(post.getMessage());
        holder.postheader.setText(post.getHeader());
        holder.fbpostpicicon.setImageResource(post.getImageDrawable());
        Log.v("FBMessage", post.getMessage());
        Log.v("FBPic", post.getURL());

        if(post.getURL().trim().startsWith("http")){
            holder.fbpostpic.setHeightRatio(1);
            Picasso.with(context).load(post.getURL()).fit().centerCrop().into(holder.fbpostpic);

        } else {
            holder.fbpostpic.setVisibility(View.GONE);

        }


        return convertView;
    }

    public static class Holder{

        DynamicHeightTextView postmessage;
        TextView postheader;
        DynamicHeightImageView fbpostpic;
        DynamicHeightImageView fbpostpicicon;

    }
}
