package in.co.mdg.campusBuddy;

import java.io.IOException;

/**
 * Created by Chirag on 03-09-2016.
 */

public class NetworkCheck {

    public static boolean isNetConnected() {
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            return mExitValue == 0;
        }
        catch (InterruptedException | IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

}
