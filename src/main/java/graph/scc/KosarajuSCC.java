package graph.scc;

import graph.Graph;
import graph.utils.Metrics;

import java.util.*;

/**
 * Kosaraju SCC implementation with metrics tracking.
 */
public class KosarajuSCC {
    private final Graph g;
    private final Metrics metrics;

    /**
     * Main constructor (required).
     */
    public KosarajuSCC(Graph g, Metrics metrics) {
        if (g == null || metrics == null) {
            throw new IllegalArgumentException("Graph and Metrics must not be null");
        }
        this.g = g;
        this.metrics = metrics;
    }

    /**
     * Factory method (optional convenience)
     */
    public static KosarajuSCC create(Graph g) {
        return new KosarajuSCC(g, new Metrics("KosarajuSCC"));
    }

    /**
     * Runs Kosaraju SCC algorithm and returns result object.
     */
    public SCCResult run() {
        metrics.startTiming();

        int n = g.n;
        boolean[] visited = new boolean[n];
        Deque<Integer> order = new ArrayDeque<>();

        // First DFS pass
        for (int i = 0; i < n; i++) {
            if (!visited[i]) dfs1(i, visited, order);
        }

        // Build reversed graph
        List<List<Graph.Edge>> radj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) radj.add(new ArrayList<>());
        for (int u = 0; u < n; u++) {
            for (Graph.Edge e : g.adj.get(u)) {
                radj.get(e.v).add(new Graph.Edge(e.v, e.u, e.w));
            }
        }

        // Second DFS pass
        Arrays.fill(visited, false);
        List<List<Integer>> components = new ArrayList<>();

        while (!order.isEmpty()) {
            int v = order.removeLast();
            if (!visited[v]) {
                List<Integer> comp = new ArrayList<>();
                dfs2(v, visited, radj, comp);
                components.add(comp);
            }
        }

        metrics.stopTiming();
        return new SCCResult(components, metrics);
    }

    private void dfs1(int u, boolean[] visited, Deque<Integer> order) {
        visited[u] = true;
        metrics.dfs1Visits++;
        for (Graph.Edge e : g.adj.get(u)) {
            metrics.dfs1Edges++;
            if (!visited[e.v]) dfs1(e.v, visited, order);
        }
        order.addLast(u);
    }

    private void dfs2(int u, boolean[] visited,
                      List<List<Graph.Edge>> radj, List<Integer> comp) {
        visited[u] = true;
        metrics.dfs2Visits++;
        comp.add(u);
        for (Graph.Edge e : radj.get(u)) {
            metrics.dfs2Edges++;
            if (!visited[e.v]) dfs2(e.v, visited, radj, comp);
        }
    }
}
