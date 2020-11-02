
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // sample mean of percolation threshold
        isValid(n, trials);
        double[] fractions = new double[trials];
        for (int trial = 0; trial < trials; trial++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                p.open(row, col);
            }
            fractions[trial] = (double) p.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(fractions);
        stddev = StdStats.stddev(fractions);
        confidenceLo = mean - (CONFIDENCE_95 * stddev) / Math.sqrt(trials);
        confidenceHi = mean + (CONFIDENCE_95 * stddev) / Math.sqrt(trials);
    }

    public double mean() {
        // sample standard deviation of percolation threshold
        return mean;
    }

    public double stddev() {
        // low endpoint of 95% confidence interval
        return stddev;
    }

    public double confidenceLo() {
        // high endpoint of 95% confidence interval
        return confidenceLo;
    }

    public double confidenceHi() {
        // test client (see below)
        return confidenceHi;
    }

    private void isValid(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trails);

        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
