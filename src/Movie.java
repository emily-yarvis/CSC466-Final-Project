import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.List;

public class Movie {
    public long budget;
    public List<Genre> genres;
    public String homepage;
    public int id;
    public String keywordsRaw;
    public String originalLanguage;
    public String originalTitle;
    public String overview;
    public double popularity;
    public String productionCompaniesRaw;
    public String productionCountriesRaw;
    public String releaseDate;
    public long revenue;
    public int runtime;
    public String spokenLanguagesRaw;
    public String status;
    public String tagline;
    public String title;
    public double voteAverage;
    public int voteCount;

    // optional: helpers/getters if you want
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", budget=" + budget +
                ", runtime=" + runtime +
                ", voteAverage=" + voteAverage +
                ", genres=" + genres +
                '}';
    }






}
