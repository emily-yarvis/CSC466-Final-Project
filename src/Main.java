import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        int[][] data = process("src/labs/Lab7/" +
                "data.txt");


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

}