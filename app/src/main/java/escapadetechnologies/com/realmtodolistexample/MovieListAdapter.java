package escapadetechnologies.com.realmtodolistexample;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmResults;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>{


    ArrayList<HashMap<String,String>> moviesArrayList;
    Context context;
    RealmResults<MovieDataList> realmResults;
    boolean autoUpdate;

    public static final String IMAGE_URL_BASE_PATH = "http://image.tmdb.org/t/p/w342//";

    /*public MovieListAdapter(Context context, RealmResults<MovieDataList> realmResults, boolean autoUpdate) {
        this.context = context;
        this.realmResults = realmResults;
        this.autoUpdate = autoUpdate;
    }*/


    public MovieListAdapter(ArrayList<HashMap<String, String>> movieDataLists, Context context) {
        this.moviesArrayList = movieDataLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movies_cardview,viewGroup,false);
        return new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewHolder movieListViewHolder, int i) {

        HashMap<String,String> hashMap = moviesArrayList.get(i);

        movieListViewHolder.overview.setText(hashMap.get("overview"));
        movieListViewHolder.popularity.setText(hashMap.get("popularity"));
        movieListViewHolder.title.setText(hashMap.get("title"));
        movieListViewHolder.id.setText(hashMap.get("id"));
        Picasso.get().load(IMAGE_URL_BASE_PATH + hashMap.get("poster_path")).into(movieListViewHolder.posterPath);


        /*final MovieDataList movieDataList = realmResults.get(i);

        movieListViewHolder.id.setText(movieDataList.getId());
        movieListViewHolder.overview.setText(movieDataList.getOverView());
        movieListViewHolder.popularity.setText(movieDataList.getPopularity());
        movieListViewHolder.title.setText(movieDataList.getTitle());

        Picasso.get().load(IMAGE_URL_BASE_PATH + movieDataList.getBackdrop_path()).into(movieListViewHolder.posterPath);*/

    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }

    public class MovieListViewHolder extends RecyclerView.ViewHolder{

        ImageView posterPath;
        TextView title,popularity,overview,id;

        public MovieListViewHolder(@NonNull View itemView) {
            super(itemView);

            posterPath = itemView.findViewById(R.id.poster_path);
            title = itemView.findViewById(R.id.title);
            popularity = itemView.findViewById(R.id.popularity);
            overview = itemView.findViewById(R.id.over_view);
            id = itemView.findViewById(R.id.id);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(context,OfflineDataActivity.class));
                }
            });

        }
    }
}
