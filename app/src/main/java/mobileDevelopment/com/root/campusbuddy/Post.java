package mobileDevelopment.com.root.campusbuddy;

import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 13/6/15.
 */
public  class Post implements Comparable<Post>{

    JSONObject post;
    String message,url,url2;
    public Date date;

    String id;

    int[] images;


    public Post(JSONObject obj)
    {
        images = new int[21];
        images[0] = R.drawable.cinema_club;
        images[1] = R.drawable.mdg;
        images[2] = R.drawable.sds_labs;
        images[3] = R.drawable.robocon;
        images[4] = R.drawable.edc;
        images[5] = R.drawable.general_nb;
        images[6] = R.drawable.audio;
        images[7] = R.drawable.sanskriti;
        images[8] = R.drawable.interactive_learning;
        images[9] = R.drawable.ashrae;
        images[10] = R.drawable.cogni;
        images[11] = R.drawable.photography;
        images[12] = R.drawable.iit_roorkee;
        images[13] = R.drawable.technologic;
        images[14] = R.drawable.electronics_section;
        images[15] = R.drawable.ncc;
        images[16] = R.drawable.cinematic;
        images[17] = R.drawable.fine_arts;
        images[18] = R.drawable.anushruti;
        images[19] = R.drawable.rhapsody;
        images[20] = R.drawable.share;


        try{
        this.post=obj;
        message = post.optString("message");
        url=post.optString("picture");
            url2=post.optString("icon");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        date = sdf.parse(post.optString("updated_time").substring(0, 10));
            id=post.getJSONObject("from").getString("name");
        }
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
    public String getHeader(){return id+"";}
    public String getURL2()
    {
        return url2;
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
