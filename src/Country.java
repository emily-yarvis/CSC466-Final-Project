import java.util.List;

public class Country {
    private String isoCode;
    private String name;

    public Country(String isoCode, String name) {
        this.isoCode = isoCode;
        this.name = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getName() {
        return name;
    }

    public int[] oneHot(List<String> allCountries) {
        int[] vector = new int[allCountries.size()];
        for (int i = 0; i < allCountries.size(); i++) {
            vector[i] = allCountries.get(i).equalsIgnoreCase(isoCode) ? 1 : 0;
        }
        return vector;
    }
}
