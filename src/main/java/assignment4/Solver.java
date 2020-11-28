
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;

public class Solver {

    private Node goalNode;
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> detect = new MinPQ<>();
        pq.insert(new Node(initial, 0, null));
        detect.insert(new Node(initial.twin(), 0, null));
        while (!pq.min().board.isGoal() && !detect.min().board.isGoal()) {
            Node cur = pq.delMin();
            Node detectCur = detect.delMin();
            for (Board nb : cur.board.neighbors()) {
                if (cur.prev == null || !nb.equals(cur.prev.board)) {
                    pq.insert(new Node(nb, cur.moves + 1, cur));
                }
            }
            for (Board nb : detectCur.board.neighbors()) {
                if (detectCur.prev == null || !nb.equals(detectCur.prev.board)) {
                    detect.insert(new Node(nb, detectCur.moves + 1, detectCur));
                }
            }
        }
        if (pq.min().board.isGoal()) {
            moves = pq.min().moves;
            goalNode = pq.min();
        } else {
            moves = -1;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return moves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Stack<Board> solution = new Stack<>();
        Node node = goalNode;
        while (node != null) {
            solution.push(node.board);
            node = node.prev;
        }
        return solution;
    }

    private class Node implements Comparable<Node> {

        private final Board board;
        private final int moves;
        private final Node prev;
        private int manhattanPriority = -1;

        Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }

        private int getManhattanPriority() {
            if (manhattanPriority == -1) {
                manhattanPriority = board.manhattan() + moves;
            }
            return manhattanPriority;
        }

        @Override
        public int compareTo(Node node) {
            return Integer.compare(this.getManhattanPriority(), node.getManhattanPriority());
        }
    }

    // test client (see below) 
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
