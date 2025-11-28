import java.util.List;

public class LanguageRish {
    private String isoCode;
    private String name;

    public LanguageRish(String isoCode, String name) {
        this.isoCode = isoCode;
        this.name = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getName() {
        return name;
    }

    public int[] oneHot(List<String> allLanguages) {
        int[] vector = new int[allLanguages.size()];
        for (int i = 0; i < allLanguages.size(); i++) {
            vector[i] = allLanguages.get(i).equalsIgnoreCase(isoCode) ? 1 : 0;
        }
        return vector;
    }
}
