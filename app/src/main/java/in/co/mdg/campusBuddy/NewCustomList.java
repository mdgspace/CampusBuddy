package in.co.mdg.campusBuddy;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.util.ArrayList;

import in.co.mdg.campusBuddy.fb.Page;

/**
 * Created by Harshit Bansal on 3/24/2017.
 */

public class NewCustomList extends RecyclerView.Adapter<NewCustomList.FbPagesViewHolder>{

    Context mcontext;
    ArrayList<Page> pageList;
    String[] fbImages;

    public NewCustomList(Context mcontext, ArrayList<Page> pageList,String[] fbImages){
        this.mcontext = mcontext;
        this.pageList = pageList;
        this.fbImages=fbImages;
    }
    @Override
    public FbPagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View fbPageItem = inflater.inflate(R.layout.mytextviewfb,parent,false);
        return new FbPagesViewHolder(fbPageItem);
    }

    @Override
    public void onBindViewHolder(final FbPagesViewHolder holder, int position) {
        final String fbPageName = pageList.get(position).getPage_name();
        try {
            String image = fbImages[position];
            Glide.with(mcontext).load(image).into(holder.fbPageImage);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        holder.fbPageName.setText(fbPageName);
        /*
        Bundle params = new Bundle();
        params.putBoolean("redirect",false);
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + pageId + "/picture", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                //Log.e("1234", response.getJSONObject().toString());
                                profilePicUrl[0] = response.getJSONObject().getJSONObject("data").getString("url");
                                //Log.e("1234", profilePicUrl[0]);
                                Glide.with(mcontext).load(profilePicUrl[0]).into(holder.fbPageImage);
                                holder.fbPageName.setText(fbPageName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Log.e("1234", "Response not received");
                            }
                            catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("12345", "Response not received");
                        }
                    }
                }).executeAsync();
                */
    }

    @Override
    public int getItemCount() {
        return pageList.size();
    }

    public class FbPagesViewHolder extends RecyclerView.ViewHolder{
        ImageView fbPageImage;
        TextView fbPageName;
        CheckBox cb;
        public FbPagesViewHolder(View itemView) {
            super(itemView);
            fbPageImage=(ImageView)itemView.findViewById(R.id.fb_page_image);
            fbPageName=(TextView)itemView.findViewById(R.id.textforpage);
            cb=(CheckBox)itemView.findViewById(R.id.checkBox);
        }
    }
}
