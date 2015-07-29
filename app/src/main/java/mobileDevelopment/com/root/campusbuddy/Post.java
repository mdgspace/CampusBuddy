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
    String message,url,url2;
    public Date date;
    String id;

    public Post(JSONObject obj)
    {
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
