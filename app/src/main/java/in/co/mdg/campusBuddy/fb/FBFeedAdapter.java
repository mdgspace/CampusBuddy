package in.co.mdg.campusBuddy.fb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;

import java.util.ArrayList;

import in.co.mdg.campusBuddy.R;

/**
 * @author Akshay
 * @version 1.0.0
 * @since 25-Jul-15
 */
class FBFeedAdapter extends ArrayAdapter<Post> {

//    ArrayList<Post> arrayList;
    private Context context;
    private static LayoutInflater inflater;

    FBFeedAdapter(Context context, int resource, ArrayList<Post> arrayList) {
        super(context, resource, arrayList);
//        this.arrayList = arrayList;
        addAll(arrayList);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

//    @Override
//    public int getCount() {
//        return arrayList.size();
//    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

//    @Nullable
//    @Override
//    public Post getItem(int position) {
//        return arrayList.get(position);
//    }

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
            holder.dateofpost = (TextView) convertView.findViewById(R.id.dateofpost);
            holder.fblayout = (LinearLayout) convertView.findViewById(R.id.cardviewfb);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final Post post = getItem(position);
        if (post != null) {
            holder.postmessage.setText(post.getMessage());
            holder.postheader.setText(post.getHeader());
            holder.fbpostpicicon.setImageResource(post.getImageDrawable());
            holder.dateofpost.setText(DateFormatter.getTimeAgo(post.getDateS()));
            if (post.getImageUrl() != null) {
                if (Fb.loadImages) {
                    holder.fbpostpic.setVisibility(View.VISIBLE);
                    try {
                        Glide.clear(holder.fbpostpic);
                        Glide.with(context).load(post.getImageUrl()).fitCenter().into(holder.fbpostpic);
                        holder.fbpostpic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent fullscreenImageView = new Intent(context, FullscreenImageView.class);
                                fullscreenImageView.putExtra("img_src", post.getImageUrl());
                                fullscreenImageView.putExtra("title", post.getHeader());
                                fullscreenImageView.putExtra("message", post.getMessage());
                                fullscreenImageView.putExtra("time", DateFormatter.getTimeAgo(post.getDateS()));
                                getContext().startActivity(fullscreenImageView);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        holder.fbpostpic.setVisibility(View.GONE);
                    }
                } else {
                    holder.fbpostpic.setVisibility(View.GONE);
                }
            } else {
                holder.fbpostpic.setVisibility(View.GONE);
            }

            holder.fblayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (post.getLinkUrl() != null) {
                        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getLinkUrl()));
                        getContext().startActivity(browser);
                    }
                }
            });

            holder.postmessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (post.getLinkUrl() != null) {
                        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getLinkUrl()));
                        getContext().startActivity(browser);
                    }
                }
            });
        }

        return convertView;
    }

    private static class Holder {
        DynamicHeightTextView postmessage;
        TextView postheader, dateofpost;
        DynamicHeightImageView fbpostpic;
        DynamicHeightImageView fbpostpicicon;
        LinearLayout fblayout;
    }
}
