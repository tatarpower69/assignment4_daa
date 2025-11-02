package graph.scc;

import graph.utils.Metrics;

import java.util.*;


public class SCCResult {
    private final List<List<Integer>> components;
    private final long elapsedNs;
    private final Metrics.Snapshot metrics;

    public SCCResult(List<List<Integer>> components, long elapsedNs, Metrics.Snapshot metrics) {
        this.components = components;
        this.elapsedNs = elapsedNs;
        this.metrics = metrics;
    }

    public List<List<Integer>> getComponents() { return components; }
    public long getElapsedNs() { return elapsedNs; }
    public Metrics.Snapshot getMetrics() { return metrics; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SCC count=").append(components.size()).append(", time(ns)=").append(elapsedNs).append("\n");
        for (int i = 0; i < components.size(); i++) {
            sb.append(String.format("  comp %d (size=%d): %s\n", i, components.get(i).size(), components.get(i)));
        }
        sb.append("Metrics: ").append(metrics).append("\n");
        return sb.toString();
    }
}
