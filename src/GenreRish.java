import java.util.List;

public class GenreRish {
    private int id;
    private String name;

    public GenreRish(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] oneHot(List<String> allGenres) {
        int[] vector = new int[allGenres.size()];
        for (int i = 0; i < allGenres.size(); i++) {
            vector[i] = allGenres.get(i).equalsIgnoreCase(name) ? 1 : 0;
        }
        return vector;
    }
}
