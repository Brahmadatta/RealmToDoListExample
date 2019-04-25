package Example.com.realmtodolistexample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRecyclerViewActivity extends AppCompatActivity {


    RecyclerView moviesRecyclerView;

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=b7ae4931443ae44ae879c87b191bb8e5";

    public static final String BASE_UR = "http://api.themoviedb.org/3/";

    ArrayList<HashMap<String,String>> movieslist;
    private Realm realm;
    MovieDataList list;

    private MovieListAdapter movieListAdapter;

    public static Retrofit retrofit = null;
    private RecyclerView recyclerView = null;

    private Movies movies;


    public static final String API_KEY = "b7ae4931443ae44ae879c87b191bb8e5";
    RepositoryMovies repositoryMovies;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_recycler_view);


        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);

        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);


        getTheData();



       /* recyclerView = findViewById(R.id.moviesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        //connectAndGetApiData();
    }

    private void connectAndGetApiData() {

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_UR)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        MovieApiService movieApiService = retrofit.create(MovieApiService.class);

        Call<MovieResponse> call = movieApiService.getTopRatedMovies(API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, retrofit2.Response<MovieResponse> response) {
                List<Movie> movies = response.body().getResults();

                //recyclerView.setAdapter(new MovieListAdapter(getApplicationContext(), movies, R.layout.movies_cardview));

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //saveDatatoRealmDatabase();
                    }
                }, 1000);

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });

    }

    private void saveDatatoRealmDatabase() {

        movies = retrofit.create(Movies.class);

        try {

            Call<List<Movie>> call = movies.movies();
            retrofit2.Response<List<Movie>> tasks = call.execute();

            for (Movie m : tasks.body()){
                repositoryMovies.addMovie(m);
            }


            List<Movie> list = repositoryMovies.readAllMovies();
            Log.e("listrealm",list.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getTheData() {

        StringRequest stringRequest = new StringRequest(BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);



                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int total_results = jsonObject.getInt("total_results");
                    int total_pages = jsonObject.getInt("total_pages");
                    String results = jsonObject.getString("results");

                    JSONArray jsonArray = new JSONArray(results);
                    movieslist = new ArrayList<>();

                    for (int i = 0 ; i < jsonArray.length() ; i++){



                        HashMap<String,String> hashMap = new HashMap<>();

                        JSONObject object = jsonArray.getJSONObject(i);

                        String poster_path = object.getString("poster_path");
                        String title = object.getString("title");
                        String popularity = object.getString("popularity");
                        String over_view = object.getString("overview");
                        String id = object.getString("id");

                        hashMap.put("poster_path",poster_path);
                        hashMap.put("title",title);
                        hashMap.put("popularity",popularity);
                        hashMap.put("overview",over_view);
                        hashMap.put("id",id);

                        movieslist.add(hashMap);



                        /*movieListAdapter = new MovieListAdapter(MovieRecyclerViewActivity.this,movieDataLists,true);
                        moviesRecyclerView.setAdapter(movieListAdapter);*/


                        //getRealmData(hashMap);



                        saveDataToRealm(movieslist);


                    }

                    attachAdapter(movieslist);

                    /*if (realm.where(MovieDataList.class).findAll().size() == 0){
                        saveToRealm(movieslist.size());
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getClass().equals(TimeoutError.class)){
                    Toast.makeText(MovieRecyclerViewActivity.this, "Timeout.Please try again", Toast.LENGTH_SHORT).show();
                }else if (error.getClass().equals(NoConnectionError.class)){
                    Toast.makeText(MovieRecyclerViewActivity.this, "Timeout.Please try again", Toast.LENGTH_SHORT).show();
                }
            }

        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void saveDataToRealm(ArrayList<HashMap<String, String>> movieslist) {

        MovieDataList list = new MovieDataList();



        for (int i = 0 ; i < movieslist.size() ; i++){

            list.setTitle(movieslist.get(i).get("title"));
            list.setPopularity(movieslist.get(i).get("popularity"));
            list.setOverView(movieslist.get(i).get("overview"));
            list.setBackdrop_path(movieslist.get(i).get("poster_path"));
            list.setId(movieslist.get(i).get("id"));

        }

        try {
            realm.where(Movie.class).equalTo("id",list.getId()).findFirst().deleteFromRealm();
        }catch (Exception e){
            e.printStackTrace();
        }


        /*for (int i = 0 ; i < hashMap.size() ; i ++) {
            list.setTitle(hashMap.get("id"));
            list.setTitle(hashMap.get("title"));
            list.setPopularity(hashMap.get("popularity"));
            list.setOverView(hashMap.get("overview"));
            list.setBackdrop_path(hashMap.get("poster_path"));

            realm.beginTransaction();
            realm.copyToRealm(list);
            realm.commitTransaction();
        }*/

        //MovieDataList list = new MovieDataList();



        realm.beginTransaction();
        realm.copyToRealm(list);
        realm.commitTransaction();

    }

    public List<MovieDataList> getRealmData(HashMap<String, String> hashMap) {


        MovieDataList list = new MovieDataList();
        list.setTitle(hashMap.get("id"));
        list.setTitle(hashMap.get("title"));
        list.setPopularity(hashMap.get("popularity"));
        list.setOverView(hashMap.get("overview"));
        list.setBackdrop_path(hashMap.get("poster_path"));


        List<MovieDataList> movieDataLists = realm.copyFromRealm(realm.where(MovieDataList.class).findAll());

        realm.close();



        return movieDataLists;


        /*realm.beginTransaction();
        realm.copyToRealm(list);
        realm.commitTransaction();*/




        /*realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                MovieDataList list = new MovieDataList();
                list.setTitle(hashMap.get("id"));
                list.setTitle(hashMap.get("title"));
                list.setPopularity(hashMap.get("popularity"));
                list.setOverView(hashMap.get("overview"));
                list.setBackdrop_path(hashMap.get("poster_path"));

                realm.insertOrUpdate(list);

                *//*realm.beginTransaction();
                realm.copyToRealm(list);
                realm.commitTransaction();*//*
            }
        });*/


        /*RealmResults<MovieDataList> tasks = realm.where(MovieDataList.class).findAll();*/
        //Log.e("movieData",tasks.toString());
    }

    private void attachAdapter(ArrayList<HashMap<String,String>> movieslist) {

        MovieListAdapter movieListAdapter = new MovieListAdapter(movieslist,this);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        moviesRecyclerView.setHasFixedSize(true);
        moviesRecyclerView.setAdapter(movieListAdapter);

        //moviesRecyclerView.setAdapter(new MovieListAdapter(realm.where(MovieDataList.class).findAllAsync(), this));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        realm = null;
    }

}
