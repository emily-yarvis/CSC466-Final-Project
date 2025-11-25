import java.util.*;

public class MovieMatrix {

    private final List<Movie> movies;
    private final double[][] X;  // features
    private final double[] means;
    private final double[] stds;

    public static List<String> GENRE_SPACE = List.of(
            "Action", "Adventure", "Fantasy", "Science Fiction",
            "Drama", "Comedy", "Family", "Animation", "Thriller",
            "Crime", "Romance", "Mystery", "Horror"
    );



    public MovieMatrix(List<Movie> movies) {
        this.movies = movies;
        this.X = toFeatureMatrix(movies);

        int n = X.length;
        int d = X[0].length;

        means = new double[d];
        stds  = new double[d];

        // compute means
        for (int j = 0; j < d; j++) {
            double sum = 0;
            for (int i = 0; i < n; i++) {
                sum += X[i][j];
            }
            means[j] = sum / n;
        }

        // compute stds
        for (int j = 0; j < d; j++) {
            double sumSq = 0;
            for (int i = 0; i < n; i++) {
                double diff = X[i][j] - means[j];
                sumSq += diff * diff;
            }
            stds[j] = Math.sqrt(sumSq / n);
            if (stds[j] == 0.0) stds[j] = 1.0;  // avoid divide by zero
        }
    }

    public static Map<String,Integer> buildGenreIndex() {
        Map<String,Integer> map = new HashMap<>();
        for (int i = 0; i < GENRE_SPACE.size(); i++) {
            map.put(GENRE_SPACE.get(i).toLowerCase(), i);
        }
        return map;
    }

    public static double[][] toFeatureMatrix(List<Movie> movies) {
        Map<String,Integer> genreIndex = buildGenreIndex();

        int g = genreIndex.size();
        int extra = 4;     // runtime, popularity, voteAverage, logRevenue
        int dim = g + extra;

        double[][] X = new double[movies.size()][dim];

        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);
            double[] row = new double[dim];

            // binary classification
            for (Genre gen : m.genres) {
                Integer idx = genreIndex.get(gen.name.toLowerCase());
                if (idx != null) {
                    row[idx] = 1.0;
                }
            }

            int offset = g;

            row[offset + 0] = m.runtime;                                // maybe 100-200
            row[offset + 1] = m.popularity;                             // ~0-400
            row[offset + 2] = m.voteAverage;                            // 0-10
            row[offset + 3] = Math.log1p(m.revenue);                    // log scale

            X[i] = row;
        }

        return X;
    }

    private double z(double value, int j) {
        return (value - means[j]) / stds[j];
    }

    private double distance(double[] a, double[] b) {
        double sum = 0.0;
        for (int j = 0; j < a.length; j++) {
            double za = z(a[j], j);
            double zb = z(b[j], j);
            double diff = za - zb;
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    // Get k nearest movies to movie index `idx` (excluding idx)
    public List<Movie> recommendSimilar(int idx, int k) {
        double[] query = X[idx];
        List<Neighbor> neighbors = new ArrayList<>();

        for (int i = 0; i < X.length; i++) {
            if (i == idx) continue;
            double d = distance(query, X[i]);
            neighbors.add(new Neighbor(d, i));
        }

        neighbors.sort(Comparator.comparingDouble(n -> n.distance));

        int limit = Math.min(k, neighbors.size());
        List<Movie> result = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            result.add(movies.get(neighbors.get(i).index));
        }
        return result;
    }

    private static class Neighbor {
        double distance;
        int index;
        Neighbor(double d, int i) { this.distance = d; this.index = i; }
    }
}

