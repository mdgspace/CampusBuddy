package mobileDevelopment.com.root.campusbuddy;

/**
 * Created by root on 19/6/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13/6/15.
 */
public class MyRecyclerAdapterfb extends RecyclerView.Adapter<PostViewHolder>
{
    public List<Post> posts;
    public MyRecyclerAdapterfb(List<Post> posts)
    {
        this.posts=new ArrayList<Post>();
        this.posts.addAll(posts);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_viewfb,parent,false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        try {
            Post post = posts.get(position);
            holder.postmessage.setText(post.getMessage());
            Picasso.with(fb.c).load(post.getURL()).into(holder.fbpostpic);
        }
        catch(Exception e)
        {
            Toast.makeText(fb.c,"Error: "+e.toString(),Toast.LENGTH_LONG).show();
            Log.d("Error: ",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}

