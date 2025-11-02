package graph.topo;

import graph.Graph;
import graph.utils.Metrics;

import java.util.*;

/**
 * DFS-based Topological Sort with metrics tracking.
 */
public class DFSTopoSort {
    private final Graph g;
    private final Metrics metrics;
    private boolean hasCycle = false;

    public DFSTopoSort(Graph g, Metrics m) {
        this.g = g;
        this.metrics = m;
    }

    public TopoResult run() {
        metrics.startTiming();
        int n = g.n;

        boolean[] visited = new boolean[n];
        boolean[] onStack = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) dfs(i, visited, onStack, stack);
        }

        metrics.stopTiming();

        List<Integer> order = new ArrayList<>(stack);
        Collections.reverse(order); // convert stack → topological order

        return new TopoResult(order, !hasCycle, metrics);
    }

    private void dfs(int u, boolean[] visited, boolean[] onStack, Deque<Integer> stack) {
        visited[u] = true;
        onStack[u] = true;
        metrics.pushOps++;

        for (Graph.Edge e : g.adj.get(u)) {
            if (hasCycle) return;
            if (!visited[e.v]) {
                dfs(e.v, visited, onStack, stack);
            } else if (onStack[e.v]) {
                hasCycle = true; // detected cycle → not a DAG
            }
        }

        metrics.popOps++;
        onStack[u] = false;
        stack.push(u);
    }
}
