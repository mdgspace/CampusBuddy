package mobileDevelopment.com.root.campusbuddy;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by root on 13/6/15.
 */
public class Post {

    JSONObject post;
    String message;
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
}
