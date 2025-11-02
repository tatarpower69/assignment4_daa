package graph.dagsp;

import graph.Graph;
import graph.utils.Metrics;

import java.util.*;

/**
 * Longest path in DAG. Implemented by negating weights OR by DP maximizing over topo order.
 * Here we implement max-DP (no negation) to avoid numeric issues.
 *
 * Note: if edges can be negative and you choose to invert sign, prefer careful treatment.
 */
public class DAGLongestPath {

    /**
     * Compute longest distances from source in DAG using topological order.
     * For unreachable nodes, distance is -Inf.
     */
    public static PathResult longestPath(Graph g, int src, List<Integer> topoOrder, Metrics metrics) {
        int n = g.size();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        dist[src] = 0;

        for (int v : topoOrder) {
            if (Double.isInfinite(dist[v]) && dist[v] < 0) continue; // unreachable
            for (Graph.Edge e : g.getAdjacency().get(v)) {
                metrics.countRelaxation();
                int u = e.to;
                double nd = dist[v] + e.weight;
                if (nd > dist[u]) {
                    dist[u] = nd;
                    parent[u] = v;
                }
            }
        }

        return new PathResult(dist, parent);
    }
}
