package graph.dagsp;


import graph.Graph;
import graph.utils.Metrics;


import java.util.*;


public class DAGShortestPath {


    public static PathResult shortestPath(Graph g, int src, List<Integer> topoOrder, Metrics metrics) {
        int n = g.size();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        int[] parent = new int[n];
        Arrays.fill(parent, -1);


        dist[src] = 0;


        for (int v : topoOrder) {
            if (Double.isInfinite(dist[v])) continue;
            for (Graph.Edge e : g.getNeighbors(v)) {
                metrics.countRelaxation();
                int u = e.getTo();
                double nd = dist[v] + e.getWeight();
                if (nd < dist[u]) {
                    dist[u] = nd;
                    parent[u] = v;
                }
            }
        }


        return new PathResult(dist, parent);
    }

    private Graph g;
    private Metrics metrics;


    public DAGShortestPath() {}


    public DAGShortestPath(Graph g, Metrics metrics) {
        this.g = g; this.metrics = metrics;
    }


    public PathResult run(int src, List<Integer> topoOrder) {
        return shortestPath(g, src, topoOrder, metrics);
    }
}