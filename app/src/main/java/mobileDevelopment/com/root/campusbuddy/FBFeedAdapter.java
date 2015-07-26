package mobileDevelopment.com.root.campusbuddy;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author Akshay
 * @version 1.0.0
 * @since 25-Jul-15
 */
public class FBFeedAdapter extends BaseAdapter {

    ArrayList<Post> arrayList;
    Context context;
    private static LayoutInflater inflater;

    public FBFeedAdapter(Context context, ArrayList<Post> arrayList) {

        this.context = context;
        this.arrayList = arrayList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View row = inflater.inflate(R.layout.card_viewfb, null);

        holder.postmessage = (TextView) row.findViewById(R.id.postmessage);
        holder.postheader = (TextView) row.findViewById(R.id.fbpagename);
        holder.fbpostpic = (ImageView) row.findViewById(R.id.fbpostpic);

        Post post = arrayList.get(position);

        holder.postmessage.setText(post.getMessage());

        if(post.getURL() == null){
            holder.fbpostpic.setVisibility(View.GONE);
        }
        /*try {
            Picasso.with(context).load(post.getURL()).fit().centerCrop().into(holder.fbpostpic);
        }catch (Exception e){
            e.printStackTrace();
            holder.fbpostpic.setVisibility(View.GONE);
        }*/
        return row;
    }

    public class Holder{

        TextView postmessage;
        TextView postheader;
        ImageView fbpostpic;
    }
}
