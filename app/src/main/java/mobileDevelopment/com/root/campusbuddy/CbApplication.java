package mobileDevelopment.com.root.campusbuddy;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * @author Akshay
 * @version 1.0.0
 * @since 17-Aug-15
 */
public class CbApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }
}
