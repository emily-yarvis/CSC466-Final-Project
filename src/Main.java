import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    private static String[] parseCSVLine(String line) {
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
    private static List<Genre> parseGenres(String cell) {
        List<Genre> genres = new ArrayList<>();
        if (cell == null || cell.isEmpty()) return genres;

        // Assuming JSON-like: [{'id': 16, 'name': 'Animation'}, ...]
        String[] items = cell.replace("[","").replace("]","").split("\\},\\s*\\{");
        for (String item : items) {
            if (item.contains("name")) {
                String name = item.split("'name':")[1].replaceAll("[^a-zA-Z ]", "").trim();
                genres.add(new Genre(0, name));
            }
        }
        return genres;
    }

    private static List<ProductionCompany> parseCompanies(String cell) {
        List<ProductionCompany> companies = new ArrayList<>();
        if (cell == null || cell.isEmpty()) return companies;

        String[] items = cell.replace("[","").replace("]","").split("\\},\\s*\\{");
        for (String item : items) {
            if (item.contains("name")) {
                String name = item.split("'name':")[1].replaceAll("[^a-zA-Z ]", "").trim();
                companies.add(new ProductionCompany(0, name));
            }
        }
        return companies;
    }

    private static List<Country> parseCountries(String cell) {
        List<Country> countries = new ArrayList<>();
        if (cell == null || cell.isEmpty()) return countries;

        String[] items = cell.replace("[","").replace("]","").split("\\},\\s*\\{");
        for (String item : items) {
            if (item.contains("iso_3166_1")) {
                String iso = item.split("'iso_3166_1':")[1].replaceAll("[^A-Z]", "").trim();
                countries.add(new Country(iso, ""));
            }
        }
        return countries;
    }

    private static List<Language> parseLanguages(String cell) {
        List<Language> langs = new ArrayList<>();
        if (cell == null || cell.isEmpty()) return langs;

        String[] items = cell.replace("[","").replace("]","").split("\\},\\s*\\{");
        for (String item : items) {
            if (item.contains("iso_639_1")) {
                String iso = item.split("'iso_639_1':")[1].replaceAll("[^a-z]", "").trim();
                langs.add(new Language(iso, ""));
            }
        }
        return langs;
    }

    private static LocalDate parseDate(String cell) {
        if (cell == null || cell.isEmpty()) return null;
        try {
            return LocalDate.parse(cell);
        } catch (Exception e) {
            return null;
        }
    }

    private static Profile buildProfile(List<Movie> movies){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Your Name: ");
        String inputName = scanner.nextLine().toLowerCase();
        ArrayList<Integer> movieIdxs = new ArrayList<>();

        while(true) {
            System.out.print("Enter a movie title or Done if you have no more: ");
            String inputTitle = scanner.nextLine().toLowerCase();
            if (inputTitle.equalsIgnoreCase("Done")) {
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
            else{
                movieIdxs.add(movieIdx);
            }
        }
        scanner.close();
        return new Profile(inputName, movieIdxs);
    }

    public List<Movie> recommendForUserProfile(List<Movie> movies, Profile userProfile, int topN) {
        // TODO: Implement content-based recommendation logic
        return new ArrayList<>();
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

    private static void performContentBasedFiltering(List<Movie> movies, MovieMatrix matrix){
        System.out.println(buildProfile(movies).toString());
    }

    public static void main(String[] args) {
        // 1. Parse your CSV into a list of Movies
        List<Movie> movies = parseData("data/movies_metadata_small.csv");

        // 2. Create MovieMatrix with features, means, stds
        MovieMatrix matrix = new MovieMatrix(movies);
        Scanner scanner = new Scanner(System.in);
        try {
            while(true) {

                System.out.print("Would you like to perform knn (a) or content based filtering (b) or to quit (quit) ");
                String option = scanner.nextLine().toLowerCase();
                if(option.equalsIgnoreCase("a")){
                    performKNN(movies, matrix);
                }
                else if(option.equalsIgnoreCase("b")){
                    performContentBasedFiltering(movies, matrix);
                }
                else if(option.equalsIgnoreCase("quit")){
                    break;
                }
                else{
                    System.out.println("That is not a valid option try again");
                }
            }
        } finally {
            scanner.close();
        }
    }
}
