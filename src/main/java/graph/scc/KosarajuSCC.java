package graph.scc;

import graph.Graph;
import graph.utils.Metrics;
import graph.utils.TimerUtil;

import java.util.*;

public class KosarajuSCC {

    private final Graph graph;
    private final Metrics metrics;

    /**
     * @param graph input graph
     * @param metrics metrics object to accumulate counters
     */
    public KosarajuSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }


    public SCCResult run() {
        TimerUtil.Timer t = TimerUtil.start();
        int n = graph.size();
        boolean[] vis = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();

        // 1) DFS
        for (int v = 0; v < n; v++) {
            if (!vis[v]) dfs1(v, vis, stack);
        }


        List<List<Integer>> rev = new ArrayList<>(n);
        for (int i = 0; i < n; i++) rev.add(new ArrayList<>());

        for (int v = 0; v < n; v++) {
            for (Graph.Edge e : graph.getAdjacency().get(v)) {
                rev.get(e.to).add(v);
                metrics.countEdgeVisit(); // count edge processing for SCC
            }
        }

        Arrays.fill(vis, false);
        List<List<Integer>> components = new ArrayList<>();
        while (!stack.isEmpty()) {
            int v = stack.pollLast();
            if (!vis[v]) {
                List<Integer> comp = new ArrayList<>();
                dfs2(v, vis, rev, comp);
                components.add(comp);
            }
        }

        long elapsedNs = t.stop();
        SCCResult result = new SCCResult(components, elapsedNs, metrics.snapshot());
        return result;
    }

    private void dfs1(int v, boolean[] vis, Deque<Integer> stack) {
        vis[v] = true;
        metrics.countDfsVisit();
        for (Graph.Edge e : graph.getAdjacency().get(v)) {
            metrics.countEdgeVisit();
            if (!vis[e.to]) dfs1(e.to, vis, stack);
        }
        stack.addLast(v);
    }

    private void dfs2(int v, boolean[] vis, List<List<Integer>> rev, List<Integer> comp) {
        vis[v] = true;
        metrics.countDfsVisit();
        comp.add(v);
        for (int u : rev.get(v)) {
            metrics.countEdgeVisit();
            if (!vis[u]) dfs2(u, vis, rev, comp);
        }
    }
}
