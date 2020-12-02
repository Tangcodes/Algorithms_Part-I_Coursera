
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> set;

    // construct an empty set of points 
    public PointSET() {
        set = new TreeSet<>();
    }

    // is the set empty? 
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set 
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validate(p);
        set.add(p);
    }

    // does the set contain point p? 
    public boolean contains(Point2D p) {
        validate(p);
        return set.contains(p);
    }

    // draw all points to standard draw 
    public void draw() {
        for (Point2D point : set) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        TreeSet<Point2D> res = new TreeSet<>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                res.add(p);
            }
        }
        return res;
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        validate(p);
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;
        for (Point2D q : set) {
            double tempDistance = q.distanceSquaredTo(p);
            if (tempDistance < minDistance) {
                nearestPoint = q;
                minDistance = tempDistance;
            }
        }
        return nearestPoint;
    }

    private static void validate(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0, 0));
        StdOut.println(ps.size());
        StdOut.println(ps.isEmpty());
    }
}
