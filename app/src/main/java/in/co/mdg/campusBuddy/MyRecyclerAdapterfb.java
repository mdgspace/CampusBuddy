package in.co.mdg.campusBuddy;

/**
 * Created by root on 19/6/15.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

/**
 * Created by root on 13/6/15.
 */
public class MyRecyclerAdapterfb extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
//    String[] pages={"Cinema Club","IIT R Freshies Forum","SDS Labs","Team robocon","edc","General Notice Board"
//    ,"Audio Section","Sanskriti Club","Group for Interative Learning","ASHARE","Cognizance","Photography Section","IIT Roorkee pge",
//            "Technology 2015 page","Electronics","ncc","Cinematics SEction","Fine Arts","Anushruti","Rhapsody","ShARE IITR"};
    public List<Post> posts;
    public MyRecyclerAdapterfb(List<Post> posts)
    {
        this.posts=new ArrayList<Post>();
        this.posts.addAll(posts);
    }



    @Override
    public int getItemViewType(int position) {
        Post post = posts.get(position);
        if(post.getURL()!=null)
        return 1;

        else
            return  2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        RecyclerView.ViewHolder vh;

        switch(viewType)
        {
            case 1:v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_viewfb,parent,false);
//                    vh=new PostViewHolder(v);
                    return new PostViewHolder(v);
//                    break;

            case 2:v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_viewfbwithoutpics,parent,false);
                return new PostViewHolderwithoutfb(v);
//                return vh;
//            break;

            default:return null;
    }}


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            int viewType=getItemViewType(position);
            if(viewType==1) {
                Post post = posts.get(position);
                ((PostViewHolder)holder).postmessage.setText(post.getMessage());
                    Picasso.with(Fb.c).load(post.getURL()).fit().centerCrop().into(((PostViewHolder)holder).fbpostpic);

                Log.e(position+"",post.getURL());
            }
            else
            {
                Post post = posts.get(position);
                ((PostViewHolderwithoutfb)holder).postmessage.setText(post.getMessage());
            }
//            else
//                holder.fbpostpic.setVisibility(View.GONE);
//            holder.postheader.setText(pages[Fb.i]);
        }
        catch(Exception e)
        {
//            Toast.makeText(Fb.c,"Error: "+e.toString(),Toast.LENGTH_LONG).show();
//            Log.d("Error: ",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}

