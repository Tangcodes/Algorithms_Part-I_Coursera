
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private final int size;
    private boolean[] isOpen;
    private boolean isPercolate;
    private int numberOfOpenSites;
    private final int topSite;
    private final int bottomSite;
    private final WeightedQuickUnionUF checkPercolateUF;
    private final WeightedQuickUnionUF checkFullUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        isValid(n);
        this.size = n;
        this.isOpen = new boolean[n * n];
        this.isPercolate = false;
        this.numberOfOpenSites = 0;
        this.topSite = n * n;
        this.bottomSite = n * n + 1;
        this.checkPercolateUF = new WeightedQuickUnionUF(n * n + 2);
        this.checkFullUF = new WeightedQuickUnionUF(n * n + 1);
        for (int i = 1; i <= n; i++) {
            int index = getIndex(1, i);
            checkPercolateUF.union(topSite, index);
            checkFullUF.union(topSite, index);
        }
        for (int i = 1; i <= n; i++) {
            int index = getIndex(n, i);
            checkPercolateUF.union(bottomSite, index);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        isValid(row, col);
        int index = getIndex(row, col);
        if (!isOpen[index]) {
            isOpen[index] = true;
            numberOfOpenSites++;
        }
        for (int[] dir : dirs) {
            int r = row + dir[0];
            int c = col + dir[1];
            int i = getIndex(r, c);
            if (r <= 0 || r > size || c <= 0 || c > size || !isOpen(r, c)) {
                continue;
            }
            checkPercolateUF.union(index, i);
            checkFullUF.union(index, i);
        }
        if (checkPercolateUF.find(topSite) == checkPercolateUF.find(bottomSite)) {
            isPercolate = true;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        isValid(row, col);
        int index = getIndex(row, col);
        return isOpen[index];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        isValid(row, col);
        int index = getIndex(row, col);
        if (!isOpen[index]) {
            return false;
        }
        return checkFullUF.find(index) == checkFullUF.find(topSite);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolate;
    }

    private int getIndex(int row, int col) {
        return size * (row - 1) + (col - 1);
    }

    private void isValid(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
    }

    private void isValid(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        // test client (optional)
    }
}
