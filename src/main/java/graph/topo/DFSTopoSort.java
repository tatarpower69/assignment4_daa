package graph.topo;


import graph.Graph;
import graph.utils.Metrics;


import java.util.*;


public class DFSTopoSort {


    public static List<Integer> topoSort(Graph g, Metrics metrics) {
        int n = g.size();
        boolean[] vis = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int v = 0; v < n; v++) if (!vis[v]) dfs(v, vis, stack, g, metrics);
        List<Integer> order = new ArrayList<>();
        while (!stack.isEmpty()) order.add(stack.removeLast());
        return order;
    }


    private static void dfs(int v, boolean[] vis, Deque<Integer> stack, Graph g, Metrics metrics) {
        vis[v] = true;
        metrics.countDfsVisit();
        for (Graph.Edge e : g.getNeighbors(v)) {
            metrics.countEdgeVisit();
            if (!vis[e.getTo()]) dfs(e.getTo(), vis, stack, g, metrics);
        }
        stack.addLast(v);
    }



    private Graph graph;
    private Metrics metrics;


    public DFSTopoSort() {}


    public DFSTopoSort(Graph graph, Metrics metrics) {
        this.graph = graph; this.metrics = metrics;
    }


    public List<Integer> run() {
        return topoSort(graph, metrics);
    }


    public List<Integer> run(Graph g, Metrics meta) { return topoSort(g, meta); }
}