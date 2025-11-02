package graph.topo;

import graph.utils.Metrics;
import graph.utils.GraphLoader.Edge;

import java.util.*;

/**
 * Kahn topological sort. Returns an order of nodes or empty list if cycle.
 */
public class KahnTopoSort {

    public static List<Integer> topoSort(int n, List<Edge>[] adj, Metrics m) {
        int[] indeg = new int[n];
        for (int u=0;u<n;u++) for (Edge e: adj[u]) indeg[e.v]++;
        Deque<Integer> q = new ArrayDeque<>();
        for (int i=0;i<n;i++) if (indeg[i]==0) q.add(i);
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.pollFirst();
            m.kahnPops++;
            order.add(u);
            for (Edge e : adj[u]) {
                indeg[e.v]--;
                if (indeg[e.v]==0) q.add(e.v);
            }
        }
        if (order.size() != n) return new ArrayList<>(); // cycle
        return order;
    }
}