package graph.topo;

import graph.utils.Metrics;

import java.util.*;

/**
 * Kahn's alg.
 */
public class KahnTopoSort {

    /**
     * Top sort use Kahn
     *
     * @param adj adjacency list
     * @param metrics metrics object to record pops/pushes
     */
    public static List<Integer> topoSort(List<List<Integer>> adj, Metrics metrics) {
        int n = adj.size();
        int[] indeg = new int[n];
        for (int v = 0; v < n; v++) {
            for (int u : adj.get(v)) indeg[u]++;
        }

        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) {
            q.addLast(i);
            metrics.countPush();
        }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int v = q.removeFirst();
            metrics.countPop();
            order.add(v);
            for (int u : adj.get(v)) {
                indeg[u]--;
                if (indeg[u] == 0) {
                    q.addLast(u);
                    metrics.countPush();
                }
            }
        }
        return order;
    }
}
