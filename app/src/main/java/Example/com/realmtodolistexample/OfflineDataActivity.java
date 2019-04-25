package Example.com.realmtodolistexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfflineDataActivity extends AppCompatActivity {


    Realm realm;
    private Movies movies;

    public static Retrofit retrofit = null;
    RepositoryMovies repositoryMovies;
    public static final String BASE_UR = "http://api.themoviedb.org/3/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_data);

        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        /*if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_UR)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        movies = retrofit.create(Movies.class);

        try {

            Call<List<Movie>> call = movies.movies();
            Response<List<Movie>> tasks = call.execute();

            for (Movie m : tasks.body()){
                repositoryMovies.addMovie(m);
            }


            List<Movie> list = repositoryMovies.readAllMovies();
            Log.e("listrealm",list.toString());

        }catch (Exception e){
            e.printStackTrace();
        }*/


        RealmResults<MovieDataList> dataLists = realm.where(MovieDataList.class).findAll();

        RealmResults<MovieDataList> dataListRealmResults = realm.where(MovieDataList.class).beginGroup().contains("title","Lord").endGroup().findAll();
        Toast.makeText(this, ""+dataLists, Toast.LENGTH_SHORT).show();

    }
}
