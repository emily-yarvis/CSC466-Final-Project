import java.util.Objects;

public class Neighbor {

        private int movieId;
        private double sim;

        public Neighbor(int id, double sim) {
            this.movieId = id;
            this.sim = sim;
        }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public double getSim() {
        return sim;
    }

    public void setSim(double sim) {
        this.sim = sim;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Neighbor neighbor = (Neighbor) o;
        return movieId == neighbor.movieId && Double.compare(sim, neighbor.sim) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, sim);
    }
}
