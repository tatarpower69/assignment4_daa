package graph.scc;

import graph.utils.Metrics;

import java.util.List;

public class SCCResult {
    public final List<List<Integer>> components;
    public final long timeNs;
    public final long dfs1Visits, dfs1Edges, dfs2Visits, dfs2Edges;

    public SCCResult(List<List<Integer>> comps, Metrics m) {
        this.components = comps;
        this.timeNs = m.elapsedNs();
        this.dfs1Visits = m.dfs1Visits;
        this.dfs1Edges = m.dfs1Edges;
        this.dfs2Visits = m.dfs2Visits;
        this.dfs2Edges = m.dfs2Edges;
    }

    public int componentCount() {
        return components.size();
    }

    public double avgComponentSize() {
        return components.stream().mapToInt(List::size).average().orElse(0);
    }
}
