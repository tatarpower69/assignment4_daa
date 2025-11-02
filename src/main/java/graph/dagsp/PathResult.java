package graph.dagsp;

import java.util.*;

/**
 * Stores path and distance info.
 */
public class PathResult {
    public final long[] dist;
    public final int[] parent;
    public PathResult(long[] dist, int[] parent) { this.dist = dist; this.parent = parent; }

    public List<Integer> reconstruct(int target) {
        if (dist[target] == Long.MAX_VALUE) return Collections.emptyList();
        LinkedList<Integer> path = new LinkedList<>();
        int cur = target;
        while (cur != -1) {
            path.addFirst(cur);
            cur = parent[cur];
        }
        return path;
    }
}