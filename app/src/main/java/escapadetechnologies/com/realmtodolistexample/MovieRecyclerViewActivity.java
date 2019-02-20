package escapadetechnologies.com.realmtodolistexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class MovieRecyclerViewActivity extends AppCompatActivity {


    RecyclerView moviesRecyclerView;

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=b7ae4931443ae44ae879c87b191bb8e5";
    ArrayList<HashMap<String,String>> movieslist;
    private Realm realm;
    MovieDataList list;

    private MovieListAdapter movieListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_recycler_view);


        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);

        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);


        getTheData();
    }

    private void getTheData() {

        StringRequest stringRequest = new StringRequest(BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);


                RealmResults<MovieDataList> movieDataLists = realm.where(MovieDataList.class).findAll();




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

                        saveDataToRealm(hashMap);

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
                Log.e("error",error.getMessage());
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

    private void saveDataToRealm(HashMap<String,String> hashMap) {

        MovieDataList list = new MovieDataList();
        list.setTitle(hashMap.get("id"));
        list.setTitle(hashMap.get("title"));
        list.setPopularity(hashMap.get("popularity"));
        list.setOverView(hashMap.get("overview"));
        list.setBackdrop_path(hashMap.get("poster_path"));

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
