
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private final List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        validate(points);
        segments = new ArrayList<>();
        int len = points.length;
        if (len < 4) {
            return;
        }
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            Arrays.sort(points);
            Arrays.sort(points, i, points.length, points[i].slopeOrder());

            int count = 1;
            Point start = points[i];
            double lastSlope = start.slopeTo(points[i + 1]);
            for (int j = i + 2; j < points.length; j++) {
                double slope = start.slopeTo(points[j]);
                if (slope != lastSlope) {
                    if (count >= 3) {
                        segments.add(new LineSegment(start, points[j - 1]));
                    }
                    count = 1;
                } else {
                    // the loop terminates with the last possible segment unchecked
                    count++;
                }
                lastSlope = slope;
            }
            if (count >= 3) {
                segments.add(new LineSegment(start, points[points.length - 1]));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
