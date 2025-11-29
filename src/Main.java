import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    /**
     * Parses a movies CSV and returns a list of MovieRish objects.
     * Adjust the indices to match your CSV column order.
     */
    private static List<Movie> parseData(String csvFilePath) {
        List<Movie> movies = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] line;
            boolean firstLine = true;

            while ((line = reader.readNext()) != null) {
                // Skip header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                try {
                    boolean adult = Boolean.parseBoolean(line[0]);
                    CollectionInfo collection = null;
                    if (!line[1].isEmpty()) {
                        collection = new CollectionInfo(0, line[1], "", "");
                    }

                    long budget = line[2].isEmpty() ? 0 : Long.parseLong(line[2]);
                    List<Genre> genres = parseGenres(line[3]);
                    String homepage = line[4];
                    int id = line[5].isEmpty() ? 0 : Integer.parseInt(line[5]);
                    String imdbId = line[6];
                    String originalLanguage = line[7];
                    String originalTitle = line[8];
                    String overview = line[9];
                    double popularity = line[10].isEmpty() ? 0 : Double.parseDouble(line[10]);
                    String posterPath = line[11];
                    List<ProductionCompany> companies = parseCompanies(line[12]);
                    List<Country> countries = parseCountries(line[13]);
                    LocalDate releaseDate = parseDate(line[14]);
                    long revenue = line[15].isEmpty() ? 0 : Long.parseLong(line[15]);
                    double runtime = line[16].isEmpty() ? 0 : Double.parseDouble(line[16]);
                    List<Language> languages = parseLanguages(line[17]);
                    String status = line[18];
                    String tagline = line[19];
                    String title = line[20];
                    boolean video = Boolean.parseBoolean(line[21]);
                    double voteAverage = line[22].isEmpty() ? 0 : Double.parseDouble(line[22]);
                    int voteCount = line[23].isEmpty() ? 0 : Integer.parseInt(line[23]);

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
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return movies;
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
        return new Profile(inputName, movieIdxs);
    }

    public List<Movie> recommendForUserProfile(List<Movie> movies,Profile userProfile, int topN) {
        List<Neighbor> neighbors = new ArrayList<>();
        return null;
    }
    private static void performKNN(List<Movie> movies, MovieMatrix matrix){
        while(true) {
            Scanner scanner = new Scanner(System.in);
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
    }

    private static void performContentBasedFiltering(List<Movie> movies, MovieMatrix matrix){
        System.out.println(buildProfile(movies).toString());
    }

    public static void main(String[] args) {
        // 1. Parse your CSV into a list of MovieRish
        List<Movie> movies = parseData("CSC466-Final-Project/data/movies_metadata_small.csv");

        // 2. Create MovieMatrixRish with features, means, stds
        MovieMatrix matrix = new MovieMatrix(movies);
        Scanner scanner = new Scanner(System.in);
        while(true) {

            System.out.print("Would you like to perform knn (a) or content based filtering (b) or to quit (quit) ");
            String option = scanner.nextLine().toLowerCase();
            if(option.equalsIgnoreCase("a")){
                performKNN(movies,matrix);
            }
            else if(option.equalsIgnoreCase("b")){
                performContentBasedFiltering(movies,matrix);
            }
            else if(option.equalsIgnoreCase("quit")){
                break;
            }
            else{
                System.out.println("That is not a valid option try again");
            }
        }
    }
}
