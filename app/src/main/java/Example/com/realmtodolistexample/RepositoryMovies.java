package Example.com.realmtodolistexample;

import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RepositoryMovies implements IRespositoryMovies {

    private Realm realm;
    private Movie movie;

    public RepositoryMovies(RealmConfiguration r) {
        realm = Realm.getInstance(r);
    }

    @Override
    public void addMovie(Movie m) {

        realm.beginTransaction();
        movie = realm.createObject(Movie.class);
        movie.setId(m.getId());
        movie.setOverview(m.getOverview());
        movie.setPopularity(m.getPopularity());
        movie.setPoster_path(m.getPoster_path());
        movie.setTitle(m.getTitle());
        realm.commitTransaction();

    }

    @Override
    public List<Movie> readAllMovies() {

        RealmResults<Movie> results = realm.where(Movie.class).findAll();
        for (Movie m : results){
            Log.e("resultmoviesr",m.getTitle());
        }
        return null;
    }
}
