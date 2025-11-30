import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.*;

public class Main {

    /**
     * Parses a movies CSV and returns a list of Movie objects.
     * Adjust the indices to match your CSV column order.
     */
    private static List<Movie> parseData(String csvFilePath) {
        List<Movie> movies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;

                boolean firstLine = true;

                while ((line = reader.readLine()) != null) {
                    // Skip header
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }

                    try {
                        String[] fields = parseCSVLine(line);

                        boolean adult = fields.length > 0 ? Boolean.parseBoolean(fields[0]) : false;
                        CollectionInfo collection = null;
                        if (fields.length > 1 && !fields[1].isEmpty()) {
                            collection = new CollectionInfo(0, fields[1], "", "");
                        }

                        long budget = fields.length > 2 && !fields[2].isEmpty() ? Long.parseLong(fields[2]) : 0;
                        List<Genre> genres = fields.length > 3 ? parseGenres(fields[3]) : new ArrayList<>();
                        String homepage = fields.length > 4 ? fields[4] : "";
                        int id = fields.length > 5 && !fields[5].isEmpty() ? Integer.parseInt(fields[5]) : 0;
                        String imdbId = fields.length > 6 ? fields[6] : "";
                        String originalLanguage = fields.length > 7 ? fields[7] : "";
                        String originalTitle = fields.length > 8 ? fields[8] : "";
                        String overview = fields.length > 9 ? fields[9] : "";
                        double popularity = fields.length > 10 && !fields[10].isEmpty() ? Double.parseDouble(fields[10]) : 0;
                        String posterPath = fields.length > 11 ? fields[11] : "";
                        List<ProductionCompany> companies = fields.length > 12 ? parseCompanies(fields[12]) : new ArrayList<>();
                        List<Country> countries = fields.length > 13 ? parseCountries(fields[13]) : new ArrayList<>();
                        LocalDate releaseDate = fields.length > 14 ? parseDate(fields[14]) : null;
                        long revenue = fields.length > 15 && !fields[15].isEmpty() ? Long.parseLong(fields[15]) : 0;
                        double runtime = fields.length > 16 && !fields[16].isEmpty() ? Double.parseDouble(fields[16]) : 0;
                        List<Language> languages = fields.length > 17 ? parseLanguages(fields[17]) : new ArrayList<>();
                        String status = fields.length > 18 ? fields[18] : "";
                        String tagline = fields.length > 19 ? fields[19] : "";
                        String title = fields.length > 20 ? fields[20] : "";
                        boolean video = fields.length > 21 ? Boolean.parseBoolean(fields[21]) : false;
                        double voteAverage = fields.length > 22 && !fields[22].isEmpty() ? Double.parseDouble(fields[22]) : 0;
                        int voteCount = fields.length > 23 && !fields[23].isEmpty() ? Integer.parseInt(fields[23]) : 0;

                        Movie movie = new Movie(
                                adult, collection, budget, genres, homepage, id, imdbId,
                                originalLanguage, originalTitle, overview, popularity,
                                posterPath, companies, countries, releaseDate, revenue,
                                runtime, languages, status, tagline, title, video,
                                voteAverage, voteCount
                        );

                        movies.add(movie);

                    } catch (Exception e) {
                        System.err.println("Error parsing line: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movies;
        }

        /**
         * Simple CSV line parser that handles quoted fields and commas within quotes
         */
        private static String[] parseCSVLine (String line){
            List<String> fields = new ArrayList<>();
            StringBuilder current = new StringBuilder();
            boolean inQuotes = false;

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                if (c == '"') {
                    inQuotes = !inQuotes;
                } else if (c == ',' && !inQuotes) {
                    fields.add(current.toString().trim());
                    current = new StringBuilder();
                } else {
                    current.append(c);
                }
            }

            fields.add(current.toString().trim());
            return fields.toArray(new String[0]);
        }

        // Helper parsers (simplified, adjust according to your CSV format)
        private static List<Genre> parseGenres (String cell){
            List<Genre> genres = new ArrayList<>();
            if (cell == null || cell.isEmpty()) return genres;

            // Assuming JSON-like: [{'id': 16, 'name': 'Animation'}, ...]
            String[] items = cell.replace("[", "").replace("]", "").split("\\},\\s*\\{");
            for (String item : items) {
                if (item.contains("name")) {
                    String name = item.split("'name':")[1].replaceAll("[^a-zA-Z ]", "").trim();
                    genres.add(new Genre(0, name));
                }
            }
            return genres;
        }

        private static List<ProductionCompany> parseCompanies (String cell){
            List<ProductionCompany> companies = new ArrayList<>();
            if (cell == null || cell.isEmpty()) return companies;

            String[] items = cell.replace("[", "").replace("]", "").split("\\},\\s*\\{");
            for (String item : items) {
                if (item.contains("name")) {
                    String name = item.split("'name':")[1].replaceAll("[^a-zA-Z ]", "").trim();
                    companies.add(new ProductionCompany(0, name));
                }
            }
            return companies;
        }

        private static List<Country> parseCountries (String cell){
            List<Country> countries = new ArrayList<>();
            if (cell == null || cell.isEmpty()) return countries;

            String[] items = cell.replace("[", "").replace("]", "").split("\\},\\s*\\{");
            for (String item : items) {
                if (item.contains("iso_3166_1")) {
                    String iso = item.split("'iso_3166_1':")[1].replaceAll("[^A-Z]", "").trim();
                    countries.add(new Country(iso, ""));
                }
            }
            return countries;
        }

        private static List<Language> parseLanguages (String cell){
            List<Language> langs = new ArrayList<>();
            if (cell == null || cell.isEmpty()) return langs;

            String[] items = cell.replace("[", "").replace("]", "").split("\\},\\s*\\{");
            for (String item : items) {
                if (item.contains("iso_639_1")) {
                    String iso = item.split("'iso_639_1':")[1].replaceAll("[^a-z]", "").trim();
                    langs.add(new Language(iso, ""));
                }
            }
            return langs;
        }

        private static LocalDate parseDate (String cell){
            if (cell == null || cell.isEmpty()) return null;
            try {
                return LocalDate.parse(cell);
            } catch (Exception e) {
                return null;
            }
        }


    private static void performKNN(List<Movie> movies, MovieMatrix matrix){
        Scanner scanner = new Scanner(System.in);
        try {
            while(true) {
                System.out.print("Enter a movie title: ");
                String inputTitle = scanner.nextLine().toLowerCase();
                if(inputTitle.equalsIgnoreCase("Quit")){
                    break;
                }
                Movie selectedMovie = null;
                int movieIdx = -1;
                for (int i = 0; i < movies.size(); i++) {
                    if (movies.get(i).getTitle().toLowerCase().equals(inputTitle)) {
                        selectedMovie = movies.get(i);
                        System.out.println("Real rating: " + selectedMovie.getVoteAverage());
                        movieIdx = i;
                        break;
                    }
                }

                if (selectedMovie == null) {
                    System.out.println("Movie not found in dataset.");

                }
                else {

                    // 5. Predict rating using KNN (choose k, e.g., 5)
                    int k = 5;
                    double predictedRating = matrix.predictRating(movieIdx, k);

                    System.out.println("Predicted rating for \"" + selectedMovie.getTitle() + "\": " + predictedRating);
                }
            }
        } finally {
            scanner.close();
        }
    }


        public static void main (String[]args){
            // 1. Parse your CSV into a list of Movies
            List<Movie> movies = parseData("data/movies_metadata_small.csv");

            // 2. Create MovieMatrix with features, means, stds
            MovieMatrix matrix = new MovieMatrix(movies);
            try {
                exportFeaturesForPCA(movies, matrix, "CSC466-Final-Project/data/movie_features_for_pca.csv");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Scanner scanner = new Scanner(System.in);
            try {
                while (true) {

                    System.out.print("Would you like to perform knn (a) or K-Means Clustering (b) or to quit (quit) ");
                    String option = scanner.nextLine().toLowerCase();
                    if (option.equalsIgnoreCase("a")) {
                        performKNN(movies, matrix);
                    } else if (option.equalsIgnoreCase("b")) {
                        performKMeansClustering(movies, matrix);
                    } else if (option.equalsIgnoreCase("quit")) {
                        break;
                    } else {
                        System.out.println("That is not a valid option try again");
                    }

                }
            } finally {
                scanner.close();
            }
        }
/// /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //KMeansClustering Utils


    private static void performKMeansClustering(List<Movie> movies, MovieMatrix matrix) {
        double[][] features = matrix.getFeatures();
        int kClusters = 20;   //adjustable
        int maxIters  = 100;
        double minImprovement = 1e-4; 

            KMeansResult result = kMeans(features, kClusters, maxIters, minImprovement);

            System.out.println("K-means finished:");
            System.out.println("  k          = " + kClusters);
            System.out.println("  final SSE  = " + result.finalSSE);

            int[] assignments = result.assignments;


        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a movie title for k-means prediction (or 'quit'): ");
            String inputTitle = scanner.nextLine().trim();
            if (inputTitle.equalsIgnoreCase("quit")){
                break;
            }

                int movieIdx = findMovieIndexByTitle(movies, inputTitle);
                if (movieIdx == -1) {
                    System.out.println("Movie not found in dataset.");
                    continue;
                }

                Movie m = movies.get(movieIdx);
                double trueRating = m.getVoteAverage();
                double predicted = predictRatingFromCluster(movieIdx, assignments, movies);

                System.out.println("Movie: " + m.getTitle());
                System.out.println("  True rating :       " + trueRating);
                System.out.println("  K-means cluster-based prediction: " + predicted);
            }
        }


        private static double predictRatingFromCluster ( int movieIdx, int[] assignments, List<Movie > movies){
            int cluster = assignments[movieIdx];

            double sum = 0.0;
            int count = 0;
            //average ratings within given cluster
            for (int i = 0; i < movies.size(); i++) {
                if (i != movieIdx && assignments[i] == cluster) {

                    sum += movies.get(i).getVoteAverage();
                    count++;
                }
            }

            return sum / count;
        }

        private static int findMovieIndexByTitle (List < Movie > movies, String inputTitle){
            String tLower = inputTitle.toLowerCase();
            for (int i = 0; i < movies.size(); i++) {
                String title = movies.get(i).getTitle();
                if (title != null && title.toLowerCase().equals(tLower)) {
                    return i;
                }
            }
            return -1;
        }
        private static void exportFeaturesForPCA (List < Movie > movies, MovieMatrix matrix, String outPath)
            throws IOException {

            double[][] features = matrix.getFeatures();

            try (PrintWriter pw = new PrintWriter(new FileWriter(outPath))) {

                pw.print("title");
                for (int i = 0; i < features[0].length; i++) {
                    pw.print(",f" + i);
                }
                pw.println();

                for (int i = 0; i < movies.size(); i++) {
                    Movie m = movies.get(i);
                    pw.print("\"" + m.getTitle().replace("\"", "") + "\"");

                    for (double x : features[i]) {
                        pw.print("," + x);
                    }
                    pw.println();
                }
            }

            System.out.println("Exported PCA features to: " + outPath);
        }

        private static KMeansResult kMeans ( double[][] matrix, int k, int maxIters, double minImprovement){
            int n = matrix.length;
            int dimensions = matrix[0].length;

            int[] assignments = new int[n];

            //initialize centroids by picking k random distinct points to start
            double[][] centroids = new double[k][dimensions];
            Random random = new Random(42);
            ArrayList<Integer> taken = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                int index = random.nextInt(n);
                while (taken.contains(index)) {
                    index = random.nextInt(n);
                }
                taken.add(index);
                centroids[i] = Arrays.copyOf(matrix[index], dimensions);
            }


            double prevSSE = 100000000;

            for (int iter = 0; iter < maxIters; iter++) {
                //assign each point to the closest centroid
                for (int i = 0; i < n; i++) {
                    double bestDist = 100000000;
                    int bestCluster = -1;
                    for (int j = 0; j < k; j++) {
                        double dist = squaredDistance(matrix[i], centroids[j]);
                        if (dist < bestDist) {
                            bestDist = dist;
                            bestCluster = j;
                        }
                    }
                    assignments[i] = bestCluster;
                }

                //recompute centroids as mean of assigned points
                double[][] newCentroids = new double[k][dimensions];
                int[] counts = new int[k];

                for (int i = 0; i < n; i++) {
                    int c = assignments[i];
                    counts[c]++;
                    for (int d = 0; d < dimensions; d++) {
                        newCentroids[c][d] += matrix[i][d];
                    }
                }

                for (int c = 0; c < k; c++) {
                    if (counts[c] > 0) {
                        for (int d = 0; d < dimensions; d++) {
                            newCentroids[c][d] /= counts[c];
                        }
                    } else {
                        newCentroids[c] = Arrays.copyOf(centroids[c], dimensions);
                    }
                }

                centroids = newCentroids;

                //compute SSE to check if we get to minImprovement otherwise repeat
                double sse = computeSSE(matrix, centroids, assignments);
                double improvement = prevSSE - sse;

                System.out.printf("Iter %d, SSE = %.4f, improvement = %.6f%n",
                        iter, sse, improvement);

                if (improvement >= 0 && improvement < minImprovement) {//once we get to min improvement level stop performing KMeans
                    break;
                }
                prevSSE = sse;
            }

            double finalSSE = computeSSE(matrix, centroids, assignments);
            return new KMeansResult(assignments, centroids, finalSSE);
        }

        //calculate the euclidean dist (x-y)^2+....
        private static double squaredDistance ( double[] a, double[] b){
            double sum = 0.0;
            for (int i = 0; i < a.length; i++) {
                double diff = a[i] - b[i];
                sum += diff * diff;
            }
            return sum;
        }

        private static double computeSSE ( double[][] X, double[][] centroids, int[] assignments){
            double sse = 0.0;
            for (int i = 0; i < X.length; i++) {
                int centroid = assignments[i];
                sse += squaredDistance(X[i], centroids[centroid]);
            }
            return sse;
        }



}





