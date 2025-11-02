package graph.utils;

/**
 * Simple metrics counters for instrumentation.
 */
public class Metrics {
    private long dfsVisits = 0;
    private long edgeVisits = 0;
    private long pops = 0;
    private long pushes = 0;
    private long relaxations = 0;

    public synchronized void countDfsVisit() { dfsVisits++; }
    public synchronized void countEdgeVisit() { edgeVisits++; }
    public synchronized void countPop() { pops++; }
    public synchronized void countPush() { pushes++; }
    public synchronized void countRelaxation() { relaxations++; }

    public Snapshot snapshot() {
        return new Snapshot(dfsVisits, edgeVisits, pops, pushes, relaxations);
    }

    public static class Snapshot {
        public final long dfsVisits, edgeVisits, pops, pushes, relaxations;
        public Snapshot(long dfsVisits, long edgeVisits, long pops, long pushes, long relaxations) {
            this.dfsVisits = dfsVisits;
            this.edgeVisits = edgeVisits;
            this.pops = pops;
            this.pushes = pushes;
            this.relaxations = relaxations;
        }
        @Override public String toString() {
            return String.format("dfs=%d edges=%d pops=%d pushes=%d relax=%d", dfsVisits, edgeVisits, pops, pushes, relaxations);
        }
    }
}
