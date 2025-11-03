package graph.dagsp;


import graph.Graph;
import graph.utils.Metrics;


import java.util.*;


public class DAGLongestPath {


    public static PathResult longestPath(Graph g, int src, List<Integer> topoOrder, Metrics metrics) {
        int n = g.size();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        int[] parent = new int[n];
        Arrays.fill(parent, -1);


        dist[src] = 0;


        for (int v : topoOrder) {
            if (Double.isInfinite(dist[v]) && dist[v] < 0) continue;
            for (Graph.Edge e : g.getNeighbors(v)) {
                metrics.countRelaxation();
                int u = e.getTo();
                double nd = dist[v] + e.getWeight();
                if (nd > dist[u]) {
                    dist[u] = nd;
                    parent[u] = v;
                }
            }
        }


        return new PathResult(dist, parent);
    }
}