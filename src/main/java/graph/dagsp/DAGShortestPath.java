package graph.dagsp;

import graph.Graph;
import graph.utils.Metrics;

import java.util.*;

/**
 * Single-source shortest paths on DAG (edge-weighted).
 * Uses topological order DP.
 */
public class DAGShortestPath {

    /**
     * Compute shortest distances from source in DAG of size n.
     * Input adjacency: for each vertex, list of Graph.Edge (to, weight).
     *
     * Returns PathResult with distances and parents.
     */
    public static PathResult shortestPath(Graph g, int src, List<Integer> topoOrder, Metrics metrics) {
        int n = g.size();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        dist[src] = 0;

        // map topoOrder to its ordering to iterate in topo sequence
        for (int v : topoOrder) {
            if (Double.isInfinite(dist[v])) continue;
            for (Graph.Edge e : g.getAdjacency().get(v)) {
                metrics.countRelaxation();
                int u = e.to;
                double nd = dist[v] + e.weight;
                if (nd < dist[u]) {
                    dist[u] = nd;
                    parent[u] = v;
                }
            }
        }

        return new PathResult(dist, parent);
    }
}
