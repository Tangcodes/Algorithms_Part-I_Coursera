
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private class kdTreeNode {

        private final Point2D p;
        private final RectHV rect;
        private final boolean isVertical;
        private kdTreeNode lb, rt;              // Left bottom & Right top subtree

        kdTreeNode(Point2D p, RectHV rect, boolean isVertical) {
            this.p = p;
            this.rect = rect;
            this.isVertical = isVertical;
        }

        int compareTo(Point2D that) {
            validate(that);
            if (this.p.equals(that)) {
                return 0;
            } else if (isVertical) {
                return this.p.x() > that.x() ? 1 : -1;
            } else {
                return this.p.y() > that.y() ? 1 : -1;
            }
        }
    }

    private kdTreeNode root;
    private int n;

    // construct an empty set of points 
    public KdTree() {
        root = null;
    }

    // is the set empty? 
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set 
    public int size() {
        return n;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validate(p);
        root = insert(p, root, null, 0);
    }

    private kdTreeNode insert(Point2D p, kdTreeNode root, kdTreeNode parent, int direction) {
        if (root == null) {
            if (n++ == 0) {
                return new kdTreeNode(p, new RectHV(0, 0, 1, 1), true);
            }
            RectHV rect;
            if (parent.isVertical) {
                if (direction > 0) {
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
                } else {
                    rect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                }
            } else {
                if (direction > 0) {
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
                } else {
                    rect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
                }
            }
            return new kdTreeNode(p, rect, !parent.isVertical);
        } else {
            int cmp = root.compareTo(p);
            if (cmp > 0) {
                root.lb = insert(p, root.lb, root, cmp);
            } else if (cmp < 0) {
                root.rt = insert(p, root.rt, root, cmp);
            }
            return root;
        }
    }

    // does the set contain point p? 
    public boolean contains(Point2D p) {
        validate(p);
        return contains(p, root);
    }

    private boolean contains(Point2D p, kdTreeNode node) {
        if (node == null) {
            return false;
        }
        int cmp = node.compareTo(p);
        if (cmp > 0) {
            return contains(p, node.lb);
        } else if (cmp < 0) {
            return contains(p, node.rt);
        } else {
            return true;
        }
    }

    // draw all points to standard draw 
    public void draw() {
        draw(root);
    }

    private void draw(kdTreeNode node) {
        if (node == null) {
            return;
        }
        draw(node.lb);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        StdDraw.setPenRadius();
        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        draw(node.rt);
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        Queue<Point2D> pointQueue = new Queue<>();
        range(rect, root, pointQueue);
        return pointQueue;
    }

    private void range(RectHV rect, kdTreeNode x, Queue<Point2D> pointQueue) {
        if (x == null) {
            return;
        }
        if (rect.contains(x.p)) {
            pointQueue.enqueue(x.p);
        }
        if (x.lb != null && rect.intersects(x.lb.rect)) {
            range(rect, x.lb, pointQueue);
        }
        if (x.rt != null && rect.intersects(x.rt.rect)) {
            range(rect, x.rt, pointQueue);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        validate(p);
        if (isEmpty()) {
            return null;
        }
        return nearest(root, root.p, p);
    }

    private Point2D nearest(kdTreeNode root, Point2D nearest, Point2D p) {
        if (root != null) {
            if (p.distanceSquaredTo(root.p) < p.distanceSquaredTo(nearest)) {
                nearest = root.p;
            }
            int cmp = root.compareTo(p);
            if (cmp > 0) {
                nearest = nearest(root.lb, nearest, p);
                if (root.rt != null && root.rt.rect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                    nearest = nearest(root.rt, nearest, p);
                }
            } else if (cmp < 0) {
                nearest = nearest(root.rt, nearest, p);
                if (root.lb != null && root.lb.rect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                    nearest = nearest(root.lb, nearest, p);
                }
            }
        }
        return nearest;
    }

    private static void validate(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        KdTree kd = new KdTree();
        kd.insert(new Point2D(0, 0));
        StdOut.println(kd.size());
        StdOut.println(kd.isEmpty());
    }
}
