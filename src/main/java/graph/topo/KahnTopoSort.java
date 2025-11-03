package graph.topo;


import graph.Graph;
import graph.utils.Metrics;


import java.util.*;


public class KahnTopoSort {


    public static List<Integer> topoSort(Graph g, Metrics metrics) {
        int n = g.size();
        int[] indeg = new int[n];
        for (int v = 0; v < n; v++) {
            for (Graph.Edge e : g.getNeighbors(v)) indeg[e.getTo()]++;
        }


        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) { q.addLast(i); metrics.countPush(); }


        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int v = q.removeFirst(); metrics.countPop(); order.add(v);
            for (Graph.Edge e : g.getNeighbors(v)) {
                int u = e.getTo();
                indeg[u]--;
                if (indeg[u] == 0) { q.addLast(u); metrics.countPush(); }
                metrics.countEdgeVisit();
            }
        }
        return order;
    }


    // instance shim
    private Graph graph;
    private Metrics metrics;


    public KahnTopoSort() {}
    public KahnTopoSort(Graph g, Metrics metrics) { this.graph = g; this.metrics = metrics; }


    public List<Integer> run() { return topoSort(graph, metrics); }
    public List<Integer> run(Graph g, Metrics m) { return topoSort(g, m); }
}