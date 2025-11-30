public class KMeansResult {


        int[] assignments;      // cluster id for each movie
        double[][] centroids;   // final centroids
        double finalSSE;        // distortion at convergence

        KMeansResult(int[] assignments, double[][] centroids, double finalSSE) {
            this.assignments = assignments;
            this.centroids = centroids;
            this.finalSSE = finalSSE;
        }
    }


