package mobileDevelopment.com.root.campusbuddy;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
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

    String id, id1;

    String postId, imageUrl;

    ArrayList<String> listofvalues;

    int[] images;
    Fblist fblist = new Fblist();

    //TODO: Data class

    public Post(JSONObject obj) {
        images = new int[22];
        images[0] = R.drawable.cinema_club;
        images[1] = R.drawable.mdg;
        images[2] = R.drawable.sds_labs;
        images[3] = R.drawable.robocon;
        images[4] = R.drawable.img;
        images[5] = R.drawable.edc;
        images[6] = R.drawable.general_nb;
        images[7] = R.drawable.audio;
        images[8] = R.drawable.sanskriti;
        images[9] = R.drawable.interactive_learning;
        images[10] = R.drawable.ashrae;
        images[11] = R.drawable.cogni;
        images[12] = R.drawable.photography;
        images[13] = R.drawable.iit_roorkee;
        images[14] = R.drawable.technologic;
        images[15] = R.drawable.electronics_section;
        images[16] = R.drawable.ncc;
        images[17] = R.drawable.cinematic;
        images[18] = R.drawable.fine_arts;
        images[19] = R.drawable.anushruti;
        images[20] = R.drawable.rhapsody;
        images[21] = R.drawable.share;

        listofvalues = new ArrayList<>();
        listofvalues.add("231275190406200");
        listofvalues.add("415004402015833");
        listofvalues.add("182484805131346");
        listofvalues.add("257702554250168");
        listofvalues.add("353701311987");
        listofvalues.add("265096099170");
        listofvalues.add("671125706342859");
        listofvalues.add("418543801611643");
        listofvalues.add("420363998145999");
        listofvalues.add("146825225353259");
        listofvalues.add("754869404569818");
        listofvalues.add("217963184943488");
        listofvalues.add("317158211638196");
        listofvalues.add("415004402015833");
        listofvalues.add("369513426410599");
        listofvalues.add("503218879766649");
        listofvalues.add("242919515859218");
        listofvalues.add("100641016663545");
        listofvalues.add("567441813288417");
        listofvalues.add("272394492879208");
        listofvalues.add("1410660759172170");
        listofvalues.add("292035034247");


        try {
            this.post = obj;

            message = post.optString("message");
            url = post.optString("picture");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            date = sdf.parse(post.optString("updated_time").substring(0, 10));
            id = post.getJSONObject("from").getString("name");
            id1 = post.getJSONObject("from").getString("id");
            Log.e("id of fb icon", id1);
            postId = post.optString("id");

        } catch (Exception e) {
            Log.e("Post error: ", e.toString());
        }

    }

    public int getImageDrawable() {
        int a = listofvalues.indexOf(id1);
        Log.e("id of fb icon", a + "");

        return images[a];
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
        return getDate().compareTo(another.getDate());
    }

    public Date getDate() {
        return date;

    }

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
}