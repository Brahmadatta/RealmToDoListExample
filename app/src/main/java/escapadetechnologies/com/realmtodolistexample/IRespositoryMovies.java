package escapadetechnologies.com.realmtodolistexample;

import java.util.List;

public interface IRespositoryMovies {

    public void addMovie(Movie movie);
    public List<Movie> readAllMovies();

}
