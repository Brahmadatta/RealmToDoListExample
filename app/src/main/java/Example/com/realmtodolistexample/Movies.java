package Example.com.realmtodolistexample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Movies {

    @GET("results")
    Call<List<Movie>> movies();

}
