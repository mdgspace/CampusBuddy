package in.co.mdg.campusBuddy.fb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import in.co.mdg.campusBuddy.R;

/**
 * @author Akshay
 * @version 1.0.0
 * @since 25-Jul-15
 */
class FBFeedAdapter extends RecyclerView.Adapter<FBFeedAdapter.PostViewHolder> {

    private ArrayList<Post> arrayList;
    private Context context;

    FBFeedAdapter(Context context) {
        this.arrayList = new ArrayList<>();
        this.context = context;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postmessage;
        TextView postheader, dateofpost;
        ImageView fbpostpic;
        ImageView fbpostpicicon;
        LinearLayout fblayout;

        PostViewHolder(View convertView) {
            super(convertView);
            postmessage = (TextView) convertView.findViewById(R.id.postmessage);
            postmessage.setMovementMethod(LinkMovementMethod.getInstance());
            Linkify.addLinks(postmessage, Linkify.ALL);
            postheader = (TextView) convertView.findViewById(R.id.fbpagename);
            fbpostpic = (ImageView) convertView.findViewById(R.id.fbpostpic);
            fbpostpicicon = (ImageView) convertView.findViewById(R.id.fbpostpicicon);
            dateofpost = (TextView) convertView.findViewById(R.id.dateofpost);
            fblayout = (LinearLayout) convertView.findViewById(R.id.cardviewfb);
        }
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_viewfb,
                parent, false));
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, int position) {
        final Post post = arrayList.get(position);
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
                                context.startActivity(fullscreenImageView);
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
                        context.startActivity(browser);
                    }
                }
            });

            holder.postmessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (post.getLinkUrl() != null) {
                        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getLinkUrl()));
                        context.startActivity(browser);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItem(Post post) {
        arrayList.add(post);
        notifyDataSetChanged();
    }

    void addAll(ArrayList<Post> posts) {
        arrayList.addAll(posts);
        Collections.sort(arrayList);
        Collections.reverse(arrayList);
        notifyDataSetChanged();
    }

    void clear() {
        arrayList.clear();
//        notifyDataSetChanged();
    }
}
