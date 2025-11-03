package graph.scc;

import graph.utils.Metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * SCC result container — stores components, optional metrics snapshot and elapsed time.
 */
public class SCCResult {
    private final List<List<Integer>> components;
    private final Metrics metrics;
    private final long elapsedNs;

    public SCCResult(List<List<Integer>> components) {
        this(components, null, 0L);
    }

    public SCCResult(List<List<Integer>> components, Metrics metrics, long elapsedNs) {
        this.components = new ArrayList<>(components);
        this.metrics = metrics;
        this.elapsedNs = elapsedNs;
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    // compatibility (some tests call components())
    public List<List<Integer>> components() {
        return getComponents();
    }

    public Metrics getMetrics() { return metrics; }
    public long getElapsedNs() { return elapsedNs; }

    public int size() { return components.size(); }
    public boolean isEmpty() { return components.isEmpty(); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SCCs: ").append(size()).append("\n");
        for (int i = 0; i < components.size(); i++) {
            sb.append("Component ").append(i).append(": ").append(components.get(i)).append("\n");
        }
        return sb.toString();
    }
}
