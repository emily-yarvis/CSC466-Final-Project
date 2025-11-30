import java.util.*;

public class MovieMatrix {
    private final List<Movie> movies;
    private double[][] features;
    private double[] means;
    private double[] stds;

    public double[][] getFeatures() {
        return features;
    }

    // All of the following are sorted
    private List<String> GENRES;
    private List<String> COUNTRIES;
    private List<String> COLLECTION_INFOS;
    private List<String> LANGUAGES;
    private List<String> PRODUCTION_COMPANIES;

    public MovieMatrix(List<Movie> movies) {
        this.movies = movies;
        COLLECTION_INFOS = findAllCollections();
        COUNTRIES = findAllCountries();
        GENRES = findAllGenres();
        LANGUAGES = findAllLanguages();
        PRODUCTION_COMPANIES = findAllProductionCompanies();
        means = computeFeatureMeans();
        stds = computeFeatureStds(means);
        populateFeaturesMatrix();
    }

    private List<String> findAllGenres() {
        Set<String> genreSet = new HashSet<>();
        for (Movie movie : movies) {
            for (Genre g : movie.getGenres()) {
                genreSet.add(g.getName());
            }
        }
        List<String> allGenres = new ArrayList<>(genreSet);
        Collections.sort(allGenres);
        return allGenres;
    }

    private List<String> findAllLanguages() {
        Set<String> languageSet = new HashSet<>();
        for (Movie movie : movies) {
            for (Language l : movie.getSpokenLanguages()) {
                languageSet.add(l.getIsoCode());
            }
        }
        List<String> allLanguages = new ArrayList<>(languageSet);
        Collections.sort(allLanguages);
        return allLanguages;
    }

    private List<String> findAllCollections() {
        Set<String> collectionSet = new HashSet<>();
        for (Movie movie : movies) {
            CollectionInfo c = movie.getBelongsToCollection();
            if (c != null) {
                collectionSet.add(c.getName());
            }
        }
        List<String> allCollections = new ArrayList<>(collectionSet);
        Collections.sort(allCollections);
        return allCollections;
    }

    private List<String> findAllProductionCompanies() {
        Set<String> companySet = new HashSet<>();
        for (Movie movie : movies) {
            for (ProductionCompany p : movie.getProductionCompanies()) {
                companySet.add(p.getName());
            }
        }
        List<String> allCompanies = new ArrayList<>(companySet);
        Collections.sort(allCompanies);
        return allCompanies;
    }

    private List<String> findAllCountries() {
        Set<String> countrySet = new HashSet<>();
        for (Movie movie : movies) {
            for (Country c : movie.getProductionCountries()) {
                countrySet.add(c.getIsoCode());
            }
        }
        List<String> allCountries = new ArrayList<>(countrySet);
        Collections.sort(allCountries);
        return allCountries;
    }

    private double[] computeFeatureMeans() {
        int n = movies.size();
        int numFeatures = 8; // adult, budget, popularity, revenue, runtime, voteAverage, video, releaseYear
        double[] means = new double[numFeatures];

        for (Movie movie : movies) {
            means[0] += movie.isAdult() ? 1.0 : 0.0;
            means[1] += movie.getBudget();
            means[2] += movie.getPopularity();
            means[3] += movie.getRevenue();
            means[4] += movie.getRuntime();
            means[5] += movie.getVoteAverage();
            means[6] += movie.isVideo() ? 1.0 : 0.0;
            means[7] += (movie.getReleaseDate() != null) ? movie.getReleaseDate().getYear() : 0;
        }

        for (int i = 0; i < numFeatures; i++) {
            means[i] /= n;
        }

        return means;
    }

    private double[] computeFeatureStds(double[] means) {
        int n = movies.size();
        int numFeatures = 8;
        double[] stds = new double[numFeatures];

        for (Movie movie : movies) {
            stds[0] += Math.pow((movie.isAdult() ? 1.0 : 0.0) - means[0], 2);
            stds[1] += Math.pow(movie.getBudget() - means[1], 2);
            stds[2] += Math.pow(movie.getPopularity() - means[2], 2);
            stds[3] += Math.pow(movie.getRevenue() - means[3], 2);
            stds[4] += Math.pow(movie.getRuntime() - means[4], 2);
            stds[5] += Math.pow(movie.getVoteAverage() - means[5], 2);
            stds[6] += Math.pow((movie.isVideo() ? 1.0 : 0.0) - means[6], 2);
            stds[7] += Math.pow((movie.getReleaseDate() != null ? movie.getReleaseDate().getYear() : 0) - means[7], 2);
        }

        for (int i = 0; i < numFeatures; i++) {
            stds[i] = Math.sqrt(stds[i] / n);
            if (stds[i] == 0.0) stds[i] = 1.0;
        }

        return stds;
    }

    private void populateFeaturesMatrix() {
        int n = movies.size();
        int numNumeric = 8; // adult, budget, popularity, revenue, runtime, voteAverage, video, releaseYear
        int totalFeatureLength = numNumeric + GENRES.size() + LANGUAGES.size() + PRODUCTION_COMPANIES.size() + COUNTRIES.size() + COLLECTION_INFOS.size();
        features = new double[n][totalFeatureLength];

        for (int i = 0; i < n; i++) {
            Movie movie = movies.get(i);
            double[] row = new double[totalFeatureLength];
            int idx = 0;

            row[idx++] = ((movie.isAdult() ? 1.0 : 0.0) - means[0]) / stds[0];
            row[idx++] = (movie.getBudget() - means[1]) / stds[1];
            row[idx++] = (movie.getPopularity() - means[2]) / stds[2];
            row[idx++] = (movie.getRevenue() - means[3]) / stds[3];
            row[idx++] = (movie.getRuntime() - means[4]) / stds[4];
            row[idx++] = (movie.getVoteAverage() - means[5]) / stds[5];
            row[idx++] = ((movie.isVideo() ? 1.0 : 0.0) - means[6]) / stds[6];
            row[idx++] = ((movie.getReleaseDate() != null ? movie.getReleaseDate().getYear() : 0) - means[7]) / stds[7];

            for (String gName : GENRES) {
                row[idx++] = movie.getGenres().stream().anyMatch(g -> g.getName().equalsIgnoreCase(gName)) ? 1.0 : 0.0;
            }

            for (String lCode : LANGUAGES) {
                row[idx++] = movie.getSpokenLanguages().stream().anyMatch(l -> l.getIsoCode().equalsIgnoreCase(lCode)) ? 1.0 : 0.0;
            }

            for (String cName : PRODUCTION_COMPANIES) {
                row[idx++] = movie.getProductionCompanies().stream().anyMatch(p -> p.getName().equalsIgnoreCase(cName)) ? 1.0 : 0.0;
            }

            for (String coCode : COUNTRIES) {
                row[idx++] = movie.getProductionCountries().stream().anyMatch(c -> c.getIsoCode().equalsIgnoreCase(coCode)) ? 1.0 : 0.0;
            }

            for (String collName : COLLECTION_INFOS) {
                CollectionInfo col = movie.getBelongsToCollection();
                row[idx++] = (col != null && col.getName().equalsIgnoreCase(collName)) ? 1.0 : 0.0;
            }

            features[i] = row;
        }
    }

    private double euclideanDistance(int idx1, int idx2) {
        double[] vec1 = features[idx1];
        double[] vec2 = features[idx2];

        double sumSq = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            double diff = vec1[i] - vec2[i];
            sumSq += diff * diff;
        }
        return Math.sqrt(sumSq);
    }

    public double predictRating(int movieIdx, int k) {
        // Get all neighbors and sort by distance
        List<Neighbor> neighbors = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            if (i == movieIdx) continue;
            double dist = euclideanDistance(movieIdx, i);
            neighbors.add(new Neighbor(dist, i));
        }
        neighbors.sort(Comparator.comparingDouble(n -> n.distance));

        // Average the voteAverage of the top-k neighbors
        double sumRatings = 0.0;
        int count = Math.min(k, neighbors.size());
        for (int i = 0; i < count; i++) {
            int neighborIdx = neighbors.get(i).index;
            sumRatings += movies.get(neighborIdx).getVoteAverage();
        }

        return sumRatings / count;
    }

    // Helper class to store distance and index
    private static class Neighbor {
        double distance;
        int index;
        Neighbor(double distance, int index) {
            this.distance = distance;
            this.index = index;
        }
    }


}
