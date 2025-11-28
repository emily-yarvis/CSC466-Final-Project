import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainRish {

    /**
     * Parses a movies CSV and returns a list of MovieRish objects.
     * Adjust the indices to match your CSV column order.
     */
    private static List<MovieRish> parseData(String csvFilePath) {
        List<MovieRish> movies = new ArrayList<>();

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
                    CollectionInfoRish collection = null;
                    if (!line[1].isEmpty()) {
                        collection = new CollectionInfoRish(0, line[1], "", "");
                    }

                    long budget = line[2].isEmpty() ? 0 : Long.parseLong(line[2]);
                    List<GenreRish> genres = parseGenres(line[3]);
                    String homepage = line[4];
                    int id = line[5].isEmpty() ? 0 : Integer.parseInt(line[5]);
                    String imdbId = line[6];
                    String originalLanguage = line[7];
                    String originalTitle = line[8];
                    String overview = line[9];
                    double popularity = line[10].isEmpty() ? 0 : Double.parseDouble(line[10]);
                    String posterPath = line[11];
                    List<ProductionCompanyRish> companies = parseCompanies(line[12]);
                    List<CountryRish> countries = parseCountries(line[13]);
                    LocalDate releaseDate = parseDate(line[14]);
                    long revenue = line[15].isEmpty() ? 0 : Long.parseLong(line[15]);
                    double runtime = line[16].isEmpty() ? 0 : Double.parseDouble(line[16]);
                    List<LanguageRish> languages = parseLanguages(line[17]);
                    String status = line[18];
                    String tagline = line[19];
                    String title = line[20];
                    boolean video = Boolean.parseBoolean(line[21]);
                    double voteAverage = line[22].isEmpty() ? 0 : Double.parseDouble(line[22]);
                    int voteCount = line[23].isEmpty() ? 0 : Integer.parseInt(line[23]);

                    MovieRish movie = new MovieRish(
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
    private static List<GenreRish> parseGenres(String cell) {
        List<GenreRish> genres = new ArrayList<>();
        if (cell == null || cell.isEmpty()) return genres;

        // Assuming JSON-like: [{'id': 16, 'name': 'Animation'}, ...]
        String[] items = cell.replace("[","").replace("]","").split("\\},\\s*\\{");
        for (String item : items) {
            if (item.contains("name")) {
                String name = item.split("'name':")[1].replaceAll("[^a-zA-Z ]", "").trim();
                genres.add(new GenreRish(0, name));
            }
        }
        return genres;
    }

    private static List<ProductionCompanyRish> parseCompanies(String cell) {
        List<ProductionCompanyRish> companies = new ArrayList<>();
        if (cell == null || cell.isEmpty()) return companies;

        String[] items = cell.replace("[","").replace("]","").split("\\},\\s*\\{");
        for (String item : items) {
            if (item.contains("name")) {
                String name = item.split("'name':")[1].replaceAll("[^a-zA-Z ]", "").trim();
                companies.add(new ProductionCompanyRish(0, name));
            }
        }
        return companies;
    }

    private static List<CountryRish> parseCountries(String cell) {
        List<CountryRish> countries = new ArrayList<>();
        if (cell == null || cell.isEmpty()) return countries;

        String[] items = cell.replace("[","").replace("]","").split("\\},\\s*\\{");
        for (String item : items) {
            if (item.contains("iso_3166_1")) {
                String iso = item.split("'iso_3166_1':")[1].replaceAll("[^A-Z]", "").trim();
                countries.add(new CountryRish(iso, ""));
            }
        }
        return countries;
    }

    private static List<LanguageRish> parseLanguages(String cell) {
        List<LanguageRish> langs = new ArrayList<>();
        if (cell == null || cell.isEmpty()) return langs;

        String[] items = cell.replace("[","").replace("]","").split("\\},\\s*\\{");
        for (String item : items) {
            if (item.contains("iso_639_1")) {
                String iso = item.split("'iso_639_1':")[1].replaceAll("[^a-z]", "").trim();
                langs.add(new LanguageRish(iso, ""));
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

    public static void main(String[] args) {
        // 1. Parse your CSV into a list of MovieRish
        List<MovieRish> movies = parseData("data/movies_metadata_small_mil.csv");

        // 2. Create MovieMatrixRish with features, means, stds
        MovieMatrixRish matrix = new MovieMatrixRish(movies);

//        int x = 0;
//        for (MovieRish movie : movies) {
//            if (x >= 5) {
//                break;
//            }
//            System.out.println(movie.getTitle() + " " + movie.getVoteAverage());
//            x++;
//        }

        // 3. Ask user for movie title
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a movie title: ");
        String inputTitle = scanner.nextLine().toLowerCase();
        MovieRish selectedMovie = null;
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
            return;
        }

        // 5. Predict rating using KNN (choose k, e.g., 5)
        int k = 5;
        double predictedRating = matrix.predictRating(movieIdx, k);

        System.out.println("Predicted rating for \"" + selectedMovie.getTitle() + "\": " + predictedRating);
    }
}
