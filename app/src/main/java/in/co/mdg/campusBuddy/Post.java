package in.co.mdg.campusBuddy;

import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by root on 13/6/15.
 */
public class Post implements Comparable<Post> {

    JSONObject post;
    String message, url, url2;
    public Date date;
    String dateS;
    String id, id1;

    String postId, imageUrl,linkurl=null;

    //TODO: Data class

    public Post(JSONObject obj) {

        try {
            this.post = obj;

            message = post.optString("message");
            url = post.optString("picture");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
            date = sdf.parse(post.optString("created_time").substring(0, 10));
            dateS=post.optString("created_time");
            id = post.getJSONObject("from").getString("name");
            id1 = post.getJSONObject("from").getString("id");
            Log.e("id of fb icon", id1);
            postId = post.optString("id");

        } catch (Exception e) {
            Log.e("Post error: ", e.toString());
        }

    }

    public int getImageDrawable() {

        int a = 0;

        ArrayList<Page> listOfValues = Data.getFbPageList();
        for(int i=0; i<listOfValues.size();i++){
            if(id1.equals(listOfValues.get(i).getPage_id())){
                a = i;
            }
        }
        Log.e("id of fb icon", a + "");

        return Data.getImages()[a];
    }

    public String getMessage() {
        return message;
    }

    public String getURL() {
        return url;
    }

    public String getHeader() {
        return id + "";
    }

    public String getPostId(){
        return postId;
    }


    @Override
    public int compareTo(Post another) {
        return getDateS().compareTo(another.getDateS());
    }

    public Date getDate() {
        return date;

    }

    public void setLinkUrl(String linkurl){this.linkurl=linkurl;}
    public  String getLinkUrl(){
        if(linkurl!=null)
        return linkurl;
    else
    return null;}

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    private static class ImageUrlHolder{
        public String imageUrl = null;
    }

    public String getDateS()
    {
        return dateS;
    }
}