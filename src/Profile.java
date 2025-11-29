import java.util.ArrayList;

public class Profile {


    private ArrayList<Integer> likedMovies;
    private String name;

    public Profile(String name, ArrayList<Integer> likedMovies) {
        this.likedMovies = likedMovies;
        this.name = name;
    }


    public ArrayList<Integer> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(ArrayList<Integer> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
