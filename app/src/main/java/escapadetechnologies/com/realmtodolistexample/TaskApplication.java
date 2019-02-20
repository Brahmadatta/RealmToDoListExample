package escapadetechnologies.com.realmtodolistexample;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TaskApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("task.realm")
                .schemaVersion(0)
                .build();

        RealmConfiguration otherConfig = new RealmConfiguration.Builder()
                .name("otherrealm.realm")
                .schemaVersion(2)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        Realm.setDefaultConfiguration(otherConfig);
    }
}
