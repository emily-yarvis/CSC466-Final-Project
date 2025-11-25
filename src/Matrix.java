import java.util.*;

public class Matrix {
    private int[][] data;
    private int rows;
    private int cols;


    public Matrix(int[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    private int findFrequencyGivenLabel(int attribute, int value,int labelValue, ArrayList<Integer> rows){
        var count = 0;
        for(int rowNum: rows){
            var currentRow = data[rowNum];
            if (currentRow[attribute] == value && currentRow[data[0].length-1] == labelValue) count++;
        }

        return count;
    }
    private int findFrequency(int attribute, int value, ArrayList<Integer> rows){
        var count = 0;
        for(int rowNum: rows){
            var currentRow = data[rowNum];
            if (currentRow[attribute] == value) count++;
        }

        return count;
    } //Examines only the specified rows of the array. It returns the number of rows in which the element at position attribute (a number between 0 and 4) is equal to value.
    private HashSet<Integer> findDifferentValues(int attribute, ArrayList<Integer> rows){
        HashSet<Integer> vals = new HashSet<>();
        for(int rowNum: rows){
            var currentRow = data[rowNum];
            vals.add(currentRow[attribute]);
        }

        return vals;
    } //Examines only the specified rows of the array. It returns a HashSet of the different values for the specified attribute.
    private ArrayList<Integer> findRows(int attribute, int value, ArrayList<Integer> rows){

        ArrayList<Integer> matchRows = new ArrayList<>();
        for(int rowNum: rows){
            var currentRow = data[rowNum];
            if (currentRow[attribute] == value) matchRows.add(rowNum);
        }

        return matchRows;
    } //Examines only the specified rows of the array. Returns an ArrayList of the rows where the value for the attribute is equal to value.
    private double log2(double number){//returns log2 of the input
        return Math.log(number)/Math.log(2);
    }
    public double findEntropy(ArrayList<Integer> rows){
        if (rows == null || rows.isEmpty()) return 0.0;

        Map<Integer,Integer> counts = new HashMap<>();
        for (int rowNum : rows) {
            int label = data[rowNum][data[rowNum].length - 1]; // last column
            counts.merge(label, 1, Integer::sum);
        }

        double entropy = 0.0;
        int n = rows.size();
        for (int label : counts.keySet()) {
            double p = counts.get(label) / (double) n; // force double division
            if (p > 0) entropy -= p * log2(p);
        }
        return entropy;
    } //finds the entropy of the dataset that consists of the specified rows.
    private double findEntropy(int attribute, ArrayList<Integer> rows) {
        if (rows == null || rows.isEmpty()) return 0.0;

        double total = 0.0;
        int S = rows.size();

        for (int val : findDifferentValues(attribute, rows)) {
            ArrayList<Integer> subset = findRows(attribute, val, rows);
            int Sv = subset.size();
            if (Sv == 0) continue;

            // label counts within this subset
            Map<Integer,Integer> counts = new HashMap<>();
            for (int r : subset) {
                int label = data[r][data[r].length - 1];
                counts.merge(label, 1, Integer::sum);
            }

            double subsetEntropy = 0.0;
            for (int label : counts.keySet()) {
                double p = counts.get(label) / (double) Sv;
                if (p > 0) subsetEntropy -= p * log2(p);
            }

            total += (Sv / (double) S) * subsetEntropy; // weight by |S_v|/|S|
        }
        return total;
    }
    private double findGain(int attribute, ArrayList<Integer> rows){
        return findEntropy(rows) - findEntropy(attribute,rows);
    } // finds the information gain of partitioning on the attribute. Considers only the specified rows.
    public double computeIGR(int attribute, ArrayList<Integer> rows){
        if (rows == null || rows.isEmpty()) return 0.0;

        double splitInfo = 0.0;
        int S = rows.size();

        for (int val : findDifferentValues(attribute, rows)) {
            ArrayList<Integer> subset = findRows(attribute, val, rows);
            int Sv = subset.size();
            if (Sv == 0) continue;
            double p = Sv / (double) S;          // <-- double division
            splitInfo -= p * log2(p);
        }

        double gain = findGain(attribute, rows);
        return (splitInfo == 0.0) ? 0.0 : (gain / splitInfo);
    } // returns the Information Gain Ratio, where we only look at the data defined by the set of rows and we consider splitting on attribute.
    public int findMostCommonValue(ArrayList<Integer> rows){
        Map<Integer,Integer> counts = new HashMap<>();
        for(int rowNum: rows){
            var currentRow = data[rowNum];
            var keyPos = currentRow.length-1;
            if(counts.containsKey(currentRow[keyPos])){
                counts.put(currentRow[keyPos],counts.get(currentRow[keyPos]) + 1);
            }
            else{
                counts.put(currentRow[keyPos],1);
            }
        }
        return counts.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    } // returns the most common category for the dataset that is the defined by the specified rows.
    public HashMap<Integer, ArrayList<Integer>> split(int attribute, ArrayList<Integer> rows){
        HashMap<Integer, ArrayList<Integer>> parts = new HashMap<>();
        for (int rowNum : rows) {
            int val = data[rowNum][attribute];
            parts.computeIfAbsent(val, k -> new ArrayList<>()).add(rowNum);
        }
        return parts;
    } //Splits the dataset that is defined by rows on the attribute. Each element of the HashMap that is returned contains the value for the attribute and an ArrayList of rows that have this value.

    public ArrayList<Integer> findAllRows(){
        ArrayList<Integer> nums = new ArrayList<>();
        for(int i=0;i<data.length;i++){
            nums.add(i);
        }
        return nums;
    } //returns all the indices of all rows, e.g., 0,1,... up to the total number of rows -1
    public int getCategoryAttribute(){
        return data[0].length ==0? -1: data[0].length -1;
    } //returns the index of the category attribute
    public double findProb(int[] row, int category){
        double mult = 0;
        for (int i = 0; i < row.length; i++) {
            double lambda = 1.0/data.length;
            double numerator = findFrequencyGivenLabel(i,row[i],category,findAllRows()) + lambda;
            double denominator = findFrequency(data[0].length-1,category,findAllRows()) +
                    (lambda * findDifferentValues(i,findAllRows()).size() ) ;
            if(mult ==0){
                mult = numerator/denominator;
            }
            else{
                mult= mult*(numerator/denominator);
            }
        }
        var probLabel =  (double)findFrequency(data[0].length-1,category,findAllRows()) /
                (double) findAllRows().size();
        return mult * probLabel;
    } //takes as input the values for a single row, e.g., 5,3,1,2 and the category, e.g. 2. Returns the probability that the row belongs to the category using the Naïve Bayesian model.
    public int findCategory(int[] row){
        var highestProb = 0.0;
        var label = 0;
        for(var num : findAllLabelValues()){
            var currentProb = findProb(row,num);
            System.out.println("For Value "+num+": Probability is: "+currentProb);
            if(currentProb > highestProb){
                highestProb = currentProb;
                label = num;
            }
        }
        System.out.println("Expected category is: "+label);
        return label;
    } //takes as input the values for a single row, e.g., 5,3,1,2. Returns the most probable category of the row using the Naïve Bayesian Model.

    public ArrayList<Integer> findAllLabelValues(){
       ArrayList<Integer> labels = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            int val = data[i][getCategoryAttribute()];
            if(!labels.contains(val)) {
                labels.add(data[i][getCategoryAttribute()]);
            }
        }
        return labels;
    }

}
