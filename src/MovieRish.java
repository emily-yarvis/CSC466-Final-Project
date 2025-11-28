import java.time.LocalDate;
import java.util.List;

public class MovieRish {
    private boolean adult;
    private CollectionInfoRish belongsToCollection;
    private long budget;
    private List<GenreRish> genres;
    private String homepage; // not used in knn
    private int id;
    private String imdbId; // not used in knn
    private String originalLanguage; // not used in knn
    private String originalTitle; // not used in knn
    private String overview; // not used in knn
    private double popularity;
    private String posterPath; // not used in knn
    private List<ProductionCompanyRish> productionCompanies;
    private List<CountryRish> productionCountries;
    private LocalDate releaseDate;
    private long revenue;
    private double runtime;
    private List<LanguageRish> spokenLanguages;
    private String status; // not used in knn
    private String tagline; // not used in knn
    private String title; // not used in knn
    private boolean video;
    private double voteAverage;
    private int voteCount; // not used in knn

    public MovieRish(boolean adult,
                     CollectionInfoRish belongsToCollection,
                     long budget,
                     List<GenreRish> genres,
                     String homepage,
                     int id,
                     String imdbId,
                     String originalLanguage,
                     String originalTitle,
                     String overview,
                     double popularity,
                     String posterPath,
                     List<ProductionCompanyRish> productionCompanies,
                     List<CountryRish> productionCountries,
                     LocalDate releaseDate,
                     long revenue,
                     double runtime,
                     List<LanguageRish> spokenLanguages,
                     String status,
                     String tagline,
                     String title,
                     boolean video,
                     double voteAverage,
                     int voteCount) {
        this.adult = adult;
        this.belongsToCollection = belongsToCollection;
        this.budget = budget;
        this.genres = genres;
        this.homepage = homepage;
        this.id = id;
        this.imdbId = imdbId;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.productionCompanies = productionCompanies;
        this.productionCountries = productionCountries;
        this.releaseDate = releaseDate;
        this.revenue = revenue;
        this.runtime = runtime;
        this.spokenLanguages = spokenLanguages;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public boolean isAdult() {
        return adult;
    }

    public CollectionInfoRish getBelongsToCollection() {
        return belongsToCollection;
    }

    public long getBudget() {
        return budget;
    }

    public List<GenreRish> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public List<ProductionCompanyRish> getProductionCompanies() {
        return productionCompanies;
    }

    public List<CountryRish> getProductionCountries() {
        return productionCountries;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public long getRevenue() {
        return revenue;
    }

    public double getRuntime() {
        return runtime;
    }

    public List<LanguageRish> getSpokenLanguages() {
        return spokenLanguages;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }
}
