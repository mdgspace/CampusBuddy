package in.co.mdg.campusBuddy.fb;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.co.mdg.campusBuddy.Data;

/**
 * Created by root on 13/6/15.
 */
class Post implements Comparable<Post> {

    private JSONObject post;
    private String message, url;
    public Date date;
    private String dateS;
    private String id, id1;

    private String postId, imageUrl, linkurl = null;

    //TODO: Data class

    Post(JSONObject obj) {

        try {
            this.post = obj;
            Log.d("JSON", "Post() called with: obj = [" + obj.toString() + "]");
            message = post.optString("message");
            url = post.optString("picture");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
            date = sdf.parse(post.optString("created_time").substring(0, 10));
            dateS = post.optString("created_time");
            id = post.getJSONObject("from").getString("name");
            id1 = post.getJSONObject("from").getString("id");
//            Log.e("id of Fb icon", id1);
            postId = post.optString("id");

        } catch (Exception e) {
            Log.e("Post error: ", e.toString());
        }

    }

    int getImageDrawable() {
        ArrayList<Page> listOfValues = Data.getFbPageList();
        for (int i = 0; i < listOfValues.size(); i++) {
            if (id1.equals(listOfValues.get(i).getPage_id())) {
                return listOfValues.get(i).getImageId();
            }
        }
        return -1;
    }

    public String getMessage() {
        return message;
    }

    public String getURL() {
        return url;
    }

    String getHeader() {
        return id + "";
    }

    String getPostId() {
        return postId;
    }


    @Override
    public int compareTo(@NonNull Post another) {
        return getDateS().compareTo(another.getDateS());
    }

    public Date getDate() {
        return date;

    }

    void setLinkUrl(String linkurl) {
        this.linkurl = linkurl;
    }

    String getLinkUrl() {
        if (linkurl != null)
            return linkurl;
        else
            return null;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    String getImageUrl() {
        return imageUrl;
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private static class ImageUrlHolder {
        public String imageUrl = null;
    }

    String getDateS() {
        return dateS;
    }
}