package mobileDevelopment.com.root.campusbuddy;

import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 13/6/15.
 */
public  class Post implements Comparable<Post>{

    JSONObject post;
    String message,url;
    public Date date;
    int[] images;

    public Post(JSONObject obj)
    {
        images = new int[21];
        images[0] = R.drawable.cinema_club;
        images[1] = R.drawable.MDG;
        images[2] = R.drawable.SDS_labs;
        images[3] = R.drawable.Robocon;
        images[4] = R.drawable.EDC;
        images[5] = R.drawable.general_NB;
        images[6] = R.drawable.audio;
        images[7] = R.drawable.sanskriti;
        images[8] = R.drawable.interactive_learning;
        images[9] = R.drawable.ashrae;
        images[10] = R.drawable.cogni;
        images[11] = R.drawable.photography;
        images[12] = R.drawable.IIT_roorkee;
        images[13] = R.drawable.technologic;
        images[14] = R.drawable.electronics_section;
        images[15] = R.drawable.NCC;
        images[16] = R.drawable.cinematic;
        images[17] = R.drawable.fine_arts;
        images[18] = R.drawable.anushruti;
        images[19] = R.drawable.rhapsody;
        images[20] = R.drawable.Share;


        try{
        this.post=obj;
        message = post.optString("message");
        url=post.optString("picture");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        date = sdf.parse(post.optString("updated_time").substring(0, 10));}
        catch (Exception e)
        {
            Log.e("Post error: ",e.toString());
        }

    }

    public String getMessage()
    {
        return message;
    }
    public String getURL()
    {
        return url;
    }

    @Override
    public int compareTo(Post another) {
        return getDate().compareTo(another.getDate());
    }

    public Date getDate()
    {
        return date;

    }

    public void setDate(Date date)
    {
        this.date=date;
    }
}
