package graph.scc;

import graph.Graph;

import java.util.*;


public class CondensationGraph {
    private final int compCount;
    private final List<List<Graph.Edge>> compAdj;
    private final int[] compOf;

    public CondensationGraph(Graph g, List<List<Integer>> components) {
        this.compCount = components.size();
        this.compAdj = new ArrayList<>(compCount);
        for (int i = 0; i < compCount; i++) compAdj.add(new ArrayList<>());
        this.compOf = new int[g.n];
        for (int i = 0; i < components.size(); i++) {
            for (int v : components.get(i)) compOf[v] = i;
        }
        Set<Long> seen = new HashSet<>();
        for (int u = 0; u < g.n; u++) {
            for (Graph.Edge e : g.adj.get(u)) {
                int cu = compOf[u], cv = compOf[e.v];
                if (cu != cv) {
                    long key = ((long) cu << 32) | (cv & 0xffffffffL);
                    if (!seen.contains(key)) {
                        seen.add(key);
                        // weight: keep original weight
                        compAdj.get(cu).add(new Graph.Edge(cu, cv, e.w));
                    }
                }
            }
        }
    }

    public int getCompCount() { return compCount; }
    public List<List<Graph.Edge>> getCompAdj() { return compAdj; }
    public int[] getCompOf() { return compOf; }
}
