package escapadetechnologies.com.realmtodolistexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class OfflineDataActivity extends AppCompatActivity {


    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_data);

        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);


        RealmResults<MovieDataList> dataLists = realm.where(MovieDataList.class).findAll();
        Toast.makeText(this, ""+dataLists, Toast.LENGTH_SHORT).show();

    }
}
