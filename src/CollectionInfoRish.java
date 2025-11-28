import java.util.List;

public class CollectionInfoRish {
    private int id;
    private String name;
    private String posterPath;
    private String backdropPath;

    public CollectionInfoRish(int id, String name, String posterPath, String backdropPath) {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public int[] oneHot(List<String> allCollections) {
        int[] vector = new int[allCollections.size()];
        for (int i = 0; i < allCollections.size(); i++) {
            vector[i] = allCollections.get(i).equalsIgnoreCase(name) ? 1 : 0;
        }
        return vector;
    }
}
