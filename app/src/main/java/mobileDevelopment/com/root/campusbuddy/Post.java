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

    public Post(JSONObject obj)
    {
        this.post=obj;
    }

    public String getMessage()
    {
        try {
            message = post.optString("message");
        }
        catch (Exception e)
        {
            Log.d("Error: ",e.toString());
        }
        return message;
    }
    public String getURL()
    {
        try{
            url=post.optString("picture");
        }
        catch (Exception e)
        {
//            Log.d("Error: ",e.toString());
        }
        return url;
    }

    @Override
    public int compareTo(Post another) {
        return getDate().compareTo(another.getDate());
    }

    public Date getDate()
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
             date = sdf.parse(post.optString("updated_time").substring(0,10));
        }
        catch (Exception e)
        {
            Toast.makeText(fb.c,e.toString(),Toast.LENGTH_LONG).show();
        }
        return date;

    }

    public void setDate(Date date)
    {
        this.date=date;
    }
}
