package graph.scc;


import graph.Graph;
import graph.utils.Metrics;
import graph.utils.TimerUtil;


import java.util.*;


public class KosarajuSCC {


    private final Graph graph;
    private final Metrics metrics;


    public KosarajuSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }


    public SCCResult run() {
        TimerUtil.Timer t = TimerUtil.start();
        int n = graph.size();
        boolean[] vis = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();


        for (int v = 0; v < n; v++) if (!vis[v]) dfs1(v, vis, stack);


        Graph trans = graph.transpose();


        Arrays.fill(vis, false);
        List<List<Integer>> components = new ArrayList<>();
        while (!stack.isEmpty()) {
            int v = stack.pollLast();
            if (!vis[v]) {
                List<Integer> comp = new ArrayList<>();
                dfs2(v, vis, trans, comp);
                components.add(comp);
            }
        }


        metrics.snapshot();
        long elapsed = t.stop();
        return new SCCResult(components);
    }


    private void dfs1(int v, boolean[] vis, Deque<Integer> stack) {
        vis[v] = true; metrics.countDfsVisit();
        for (Graph.Edge e : graph.getNeighbors(v)) {
            metrics.countEdgeVisit();
            if (!vis[e.getTo()]) dfs1(e.getTo(), vis, stack);
        }
        stack.addLast(v);
    }


    private void dfs2(int v, boolean[] vis, Graph trans, List<Integer> comp) {
        vis[v] = true; metrics.countDfsVisit(); comp.add(v);
        for (Graph.Edge e : trans.getNeighbors(v)) {
            metrics.countEdgeVisit();
            if (!vis[e.getTo()]) dfs2(e.getTo(), vis, trans, comp);
        }
    }
}