package graph.topo;

import graph.utils.Metrics;

import java.util.*;

/**
 * DFS-based topological sort
 */
public class DFSTopoSort {

    public static List<Integer> topoSort(List<List<Integer>> adj, Metrics metrics) {
        int n = adj.size();
        boolean[] vis = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int v = 0; v < n; v++) if (!vis[v]) dfs(v, vis, stack, adj, metrics);
        List<Integer> order = new ArrayList<>();
        while (!stack.isEmpty()) {
            order.add(stack.removeLast());
        }
        return order;
    }

    private static void dfs(int v, boolean[] vis, Deque<Integer> stack, List<List<Integer>> adj, Metrics metrics) {
        vis[v] = true;
        metrics.countDfsVisit();
        for (int u : adj.get(v)) {
            metrics.countEdgeVisit();
            if (!vis[u]) dfs(u, vis, stack, adj, metrics);
        }
        stack.addLast(v);
    }
}
