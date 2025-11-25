
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class Main {
    private static final Gson GSON = new Gson();

    static public void main(String[] args) throws IOException {
        int[][] data = process("CSC466-Final-Project/data/data.txt");
        var matrix = new Matrix(data);

        System.out.println("Prediction: "+matrix.predictWithKNN(4, new int[]{0, 4, 3, 2}));
        var movies =loadMovies(Path.of("CSC466-Final-Project/data/tmdb_5000_movies.csv"));
        for(var m: movies){
            System.out.println(m.toString());
        }

        MovieMatrix recommender = new MovieMatrix(movies);

// Find index of a movie by title
        while (true) {

            int movieIdx = -1;
            String movie = getCustomerMovieName();
            if(movie.equalsIgnoreCase("Exit")){
                System.out.println("Goodbye!");
                break;
            }
            for (int i = 0; i < movies.size(); i++) {
                if (movie.equalsIgnoreCase(movies.get(i).title)) {
                    movieIdx = i;
                    break;
                }
            }

            if (movieIdx >= 0) {
                List<Movie> recs = recommender.recommendSimilar(movieIdx, 10);
                System.out.println("Because you watched: " + movies.get(movieIdx).title);
                for (Movie m : recs) {
                    System.out.println("  -> " + m.title + "  (" + m.releaseDate + ", " + m.genres + ")");
                }
            } else {
                System.out.println("I'm sorry I cant make a recommendation for that movie");
            }
        }

    }

    public static void write2OutputFile(String filename, int[][] data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    writer.write(Integer.toString(row[i]));
                    if (i < row.length - 1) writer.write(" ");
                }
                writer.newLine();
            }
        }
    }

    public static int[][] process(String filename) {
        ArrayList<int[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            for (String line; (line = br.readLine()) != null; ) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length != 5) throw new IllegalArgumentException("Bad row: " + line);

                int[] row = new int[5];
                for (int i = 0; i < 4; i++) {
                    double v = Double.parseDouble(parts[i]);
                    row[i] = (int) v;
                }
                row[4] = Integer.parseInt(parts[4]); // class label 1,2,3
                rows.add(row);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rows.toArray(new int[0][]);

    } //creates a two-dimensional array from the input file.

    public static List<Movie> loadMovies(Path csvPath) throws IOException {
        List<Movie> movies = new ArrayList<>();

        try (Reader in = Files.newBufferedReader(csvPath);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()//use header row included in data to parse
                     .withTrim()
                     .parse(in)) {

            for (CSVRecord rec : parser) {
                Movie m = new Movie();

                m.budget = parseLong(rec.get("budget"));
                m.genres = parseGenres(rec.get("genres"));
                m.homepage = emptyToNull(rec.get("homepage"));
                m.id = (int) parseLong(rec.get("id"));
                m.keywordsRaw = rec.get("keywords");
                m.originalLanguage = rec.get("original_language");
                m.originalTitle = rec.get("original_title");
                m.overview = rec.get("overview");
                m.popularity = parseDouble(rec.get("popularity"));
                m.productionCompaniesRaw = rec.get("production_companies");
                m.productionCountriesRaw = rec.get("production_countries");
                m.releaseDate = rec.get("release_date");
                m.revenue = parseLong(rec.get("revenue"));

                m.runtime = (int) Math.round(parseDouble(rec.get("runtime")));

                m.spokenLanguagesRaw = rec.get("spoken_languages");
                m.status = rec.get("status");
                m.tagline = rec.get("tagline");
                m.title = rec.get("title");
                m.voteAverage = parseDouble(rec.get("vote_average"));
                m.voteCount = (int) parseLong(rec.get("vote_count"));

                movies.add(m);
            }
        }

        return movies;
    }


    private static List<Genre> parseGenres(String json) {
        if (json == null || json.isBlank() || json.equals("[]")) {
            return List.of();
        }
        try {

            return GSON.fromJson(json, new TypeToken<List<Genre>>() {}.getType());
        } catch (Exception e) {
            System.err.println("Failed to parse genres JSON: " + json);
            e.printStackTrace();
            return List.of();
        }
    }


    private static long parseLong(String s) {
        if (s == null) return 0L;
        s = s.trim();
        if (s.isEmpty()) return 0L;
        if (s.contains(".")) {
            return (long) Double.parseDouble(s);
        }
        return Long.parseLong(s);
    }

    private static double parseDouble(String s) {
        if (s == null) return 0.0;
        s = s.trim();
        if (s.isEmpty()) return 0.0;
        return Double.parseDouble(s);
    }

    private static String emptyToNull(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    public static String getCustomerMovieName(){
        Scanner sc = new Scanner(System.in);

            System.out.print("Enter name of movie for a new recommendation: ");
            String response = sc.nextLine();


        return response;
    }
}


