package in.co.mdg.campusBuddy;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by Chirag on 03-09-2016.
 */

public class NetworkCheck {
    public static boolean isNetConnected() {
       Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            return mExitValue == 0;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static int chkStatus(ConnectivityManager connMgr) {
    //        final ConnectivityManager connMgr = (ConnectivityManager)
    //                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return 1;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return 2;
            }
        } else {
            // not connected to the internet
        }
        return 0;
    }

}
