package escapadetechnologies.com.realmtodolistexample;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Movie extends RealmObject {

    @SerializedName("title")
    private String title;

    @SerializedName("id")
    private String id;

    @SerializedName("poster_path")
    private String poster_path;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private String popularity;

    public Movie() {
    }

    public Movie(String title, String id, String poster_path, String overview, String popularity) {
        this.title = title;
        this.id = id;
        this.poster_path = poster_path;
        this.overview = overview;
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }
}
