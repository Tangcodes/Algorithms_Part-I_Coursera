
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        validate(points);
        int len = points.length;
        segments = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                double slope1 = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < len; k++) {
                    double slope2 = points[j].slopeTo(points[k]);
                    if (slope2 != slope1) {
                        continue;
                    } else {
                        for (int h = k + 1; h < len; h++) {
                            if (points[k].slopeTo(points[h]) == slope2) {
                                Point[] segment = {points[i], points[j], points[k], points[h]};
                                Arrays.sort(segment);
                                segments.add(new LineSegment(segment[0], segment[3]));
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return segments.size();
    }

    public LineSegment[] segments() {
        // the line segments
        LineSegment[] res = new LineSegment[numberOfSegments()];
        for (int i = 0; i < numberOfSegments(); i++) {
            res[i] = segments.get(i);
        }
        return res;
    }

    private static void validate(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if ((points[j].equals(points[i]))) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
