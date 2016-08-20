package in.co.mdg.campusBuddy.fcm;

/**
 * Created by mohit on 17/8/16.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

//

/**
 * Created by mohit on 12/8/16.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", token);
//        registerToken(token);
    }
//    private void registerToken(String token){
//
//        OkHttpClient client = new OkHttpClient();
//
//        RequestBody body = new FormBody.Builder()
//                .add("Token",token)
//                .build();
//
//        Request request = new Request.Builder()
//                .url("http://dev.ueuo.com/pushnotif/register.php")//replace with your sql writer
//                .post(body)
//                .build();
//
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("registerToken","success");
//            }
//        });
//
//    }
}
