package graph.dagsp;


import java.util.*;


public class PathResult {
    private final double[] dist;
    private final int[] parent;


    public PathResult(double[] dist, int[] parent) {
        this.dist = dist;
        this.parent = parent;
    }


    public double[] getDist() { return dist; }
    public int[] getParent() { return parent; }


    // backward-compatible aliases some tests expect
    public double[] getDistances() { return dist; }
    public int[] getParents() { return parent; }


    public List<Integer> reconstruct(int target) {
        if (target < 0 || target >= parent.length) return Collections.emptyList();
        if (Double.isInfinite(dist[target]) || dist[target] == Double.POSITIVE_INFINITY) return Collections.emptyList();
        LinkedList<Integer> path = new LinkedList<>();
        int cur = target;
        while (cur != -1) {
            path.addFirst(cur);
            cur = parent[cur];
        }
        return path;
    }
}