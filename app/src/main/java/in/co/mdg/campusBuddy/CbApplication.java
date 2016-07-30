package in.co.mdg.campusBuddy;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Akshay
 * @version 1.0.0
 * @since 17-Aug-15
 */
public class CbApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        RealmConfiguration config = new RealmConfiguration
                .Builder(this)
                .name("CBData.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
