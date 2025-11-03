package graph.utils;


public class Metrics {

    private int dfsVisits = 0;
    private int edgeVisits = 0;
    private int relaxations = 0;
    private int pushOps = 0;
    private int popOps = 0;
    private int operations = 0;

    private long startTime;
    private long endTime;

    // TIMER
    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }

    // COUNTERS
    public void countDfsVisit() {
        dfsVisits++;
    }

    public void countEdgeVisit() {
        edgeVisits++;
    }

    public void countRelaxation() {
        relaxations++;
    }

    public void countPush() {
        pushOps++;
    }

    public void countPop() {
        popOps++;
    }

    public void incrementOperations() {
        operations++;
    }

    public void snapshot() {
        System.out.println("--- Metrics Snapshot ---");
        System.out.println("DFS Visits: " + dfsVisits);
        System.out.println("Edge Visits: " + edgeVisits);
        System.out.println("Relaxations: " + relaxations);
        System.out.println("Push Ops: " + pushOps);
        System.out.println("Pop Ops: " + popOps);
        System.out.println("Total Operations: " + operations);
        System.out.println("Elapsed Time (ns): " + getElapsedTime());
        System.out.println("-------------------------");
    }

    public int getDfsVisits() {
        return dfsVisits;
    }

    public int getEdgeVisits() {
        return edgeVisits;
    }

    public int getRelaxations() {
        return relaxations;
    }

    public int getPushOps() {
        return pushOps;
    }

    public int getPopOps() {
        return popOps;
    }

    public int getOperationCount() {
        return operations;
    }
}
