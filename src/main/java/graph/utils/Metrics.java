package graph.utils;


public class Metrics {
    public long startNs = 0;
    public long endNs = 0;

    // SCC Kosaraju
    public long dfs1Visits = 0;
    public long dfs1Edges = 0;
    public long dfs2Visits = 0;
    public long dfs2Edges = 0;

    // Kahn
    public long kahnPushes = 0;
    public long kahnPops = 0;

    // DAG relaxations
    public long relaxations = 0;

    public void start() { startNs = System.nanoTime(); }
    public void stop() { endNs = System.nanoTime(); }
    public long elapsedNs() { return endNs - startNs; }
}
