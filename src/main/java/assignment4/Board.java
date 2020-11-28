
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Stack;

public class Board {

    private final int[][] board;
    private final int size;
    private int hammingNumber = -1;
    private int manhattanNumber = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.size = tiles.length;
        this.board = new int[size][size];
        for (int i = 0; i < size; i++) {
            this.board[i] = Arrays.copyOf(tiles[i], size);
        }
    }
    
    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size).append("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(String.format("%2d ", board[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        if (hammingNumber == -1) {
            hammingNumber = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] != 0 && board[i][j] != getGoalVal(i, j)) {
                        hammingNumber++;
                    }
                }
            }
        }
        return hammingNumber;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattanNumber == -1) {
            manhattanNumber = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] != 0 && board[i][j] != getGoalVal(i, j)) {
                        int num = board[i][j] - 1;
                        int I = num / size;
                        int J = num % size;
                        manhattanNumber += (Math.abs(i - I) + Math.abs(j - J));
                    }
                }
            }
        }
        return manhattanNumber;
    }

    private int getGoalVal(int i, int j) {
        if (i == size - 1 && j == size - 1) {
            return 0;
        }
        return i * size + j + 1;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (that.size != size) {
            return false;
        }
        return Arrays.deepEquals(this.board, that.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int idxI = 0, idxJ = 0;
        isFindBlank:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    idxI = i;
                    idxJ = j;
                    break isFindBlank;
                }
            }
        }
        Stack<Board> stack = new Stack<>();
        Board next;
        if (idxI != 0) {
            next = new Board(board);
            next.exch(idxI, idxJ, idxI - 1, idxJ);
            stack.push(next);
        }
        if (idxI != size - 1) {
            next = new Board(board);
            next.exch(idxI, idxJ, idxI + 1, idxJ);
            stack.push(next);
        }
        if (idxJ != 0) {
            next = new Board(board);
            next.exch(idxI, idxJ, idxI, idxJ - 1);
            stack.push(next);
        }
        if (idxJ != size - 1) {
            next = new Board(board);
            next.exch(idxI, idxJ, idxI, idxJ + 1);
            stack.push(next);
        }
        return stack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board newBoard = new Board(board);
        for (int i = 0; i < 2; i++) {
            if (newBoard.board[i][0] != 0 && newBoard.board[i][1] != 0) {
                newBoard.exch(i, 0, i, 1);
                return newBoard;
            }
        }
        throw new IllegalArgumentException();
    }

    private void exch(int x1, int y1, int x2, int y2) {
        assert (x1 >= 0 && x1 < size && y1 >= 0 && y1 < size && x2 >= 0 && x2 < size && y2 >= 0 && y2 < size);

        int temp = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int[][] init = new int[n][n];
        int start = n * n - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                init[i][j] = start--;
            }
        }
        Board b = new Board(init);
        StdOut.println(b);
    }
}
