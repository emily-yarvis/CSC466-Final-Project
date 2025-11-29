import java.util.List;

public class ProductionCompany {
    private int id;
    private String name;

    public ProductionCompany(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] oneHot(List<String> allCompanies) {
        int[] vector = new int[allCompanies.size()];
        for (int i = 0; i < allCompanies.size(); i++) {
            vector[i] = allCompanies.get(i).equalsIgnoreCase(name) ? 1 : 0;
        }
        return vector;
    }
}
