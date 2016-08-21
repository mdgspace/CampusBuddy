package in.co.mdg.campusBuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * @author Akshay
 * @version 1.0.0
 * @since 25-Jul-15
 */
class FBFeedAdapter extends ArrayAdapter<Post> {

    ArrayList<Post> arrayList;
    private Context context;
    private static LayoutInflater inflater;

    FBFeedAdapter(Context context, int resource, ArrayList<Post> arrayList) {
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

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Holder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.card_viewfb, parent, false);
            holder = new Holder();
            holder.postmessage = (DynamicHeightTextView) convertView.findViewById(R.id.postmessage);
            holder.postmessage.setMovementMethod(LinkMovementMethod.getInstance());
            Linkify.addLinks(holder.postmessage, Linkify.ALL);
            holder.postheader = (TextView) convertView.findViewById(R.id.fbpagename);
            holder.fbpostpic = (DynamicHeightImageView) convertView.findViewById(R.id.fbpostpic);
            holder.fbpostpicicon = (DynamicHeightImageView) convertView.findViewById(R.id.fbpostpicicon);
            holder.dateofpost=(TextView)convertView.findViewById(R.id.dateofpost);
            holder.fblayout=(LinearLayout)convertView.findViewById(R.id.cardviewfb);
            convertView.setTag(holder);
        } else {
          holder = (Holder) convertView.getTag();
        }

        final Post post = arrayList.get(position);

        holder.postmessage.setText(post.getMessage());
        holder.postheader.setText(post.getHeader());
        holder.fbpostpicicon.setImageResource(post.getImageDrawable());
        holder.dateofpost.setText(DateFormatter.getTimeAgo(post.getDateS()));

        Transformation  transformation = new Transformation(){

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = holder.fbpostpic.getWidth();

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return null;
            }
        };
        holder.fbpostpic.setVisibility(View.VISIBLE);

            if(post.getImageUrl()!=null){
                try {
                    Picasso.with(context).load(post.getImageUrl()).transform(transformation).
                            into(holder.fbpostpic);
                }catch (Exception e){
                    e.printStackTrace();
                    holder.fbpostpic.setVisibility(View.GONE);
                }
            }else{
                holder.fbpostpic.setVisibility(View.GONE);
            }

            holder.fblayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(post.getLinkUrl()!=null) {
                        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getLinkUrl()));
                        getContext().startActivity(browser);
                    }
                }
            });

        holder.postmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(post.getLinkUrl()!=null) {
                    Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getLinkUrl()));
                    getContext().startActivity(browser);
                }
            }
        });

        return convertView;
    }

    private static class Holder{

        DynamicHeightTextView postmessage;
        TextView postheader,dateofpost;
        DynamicHeightImageView fbpostpic;
        DynamicHeightImageView fbpostpicicon;
        LinearLayout fblayout;

    }
}
