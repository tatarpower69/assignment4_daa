package graph.utils;

public class Metrics {
    private long start, end;

    public long dfs1Visits = 0;
    public long dfs1Edges = 0;
    public long dfs2Visits = 0;
    public long dfs2Edges = 0;

    private final String name;

    public Metrics(String name) {
        this.name = name;
    }

    public void startTiming() {
        start = System.nanoTime();
    }

    public void stopTiming() {
        end = System.nanoTime();
    }

    public long elapsedNs() {
        return end - start;
    }

    public String getName() {
        return name;
    }
}
