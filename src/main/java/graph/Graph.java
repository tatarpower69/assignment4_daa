package graph;

import java.util.*;


public class Graph {
    private final int n;
    private final List<List<Edge>> adj;

    public Graph(int n) {
        this.n = n;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public int size() { return n; }

    public void addEdge(int from, int to, int weight) {
        adj.get(from).add(new Edge(to, weight));
    }

    public List<List<Edge>> getAdjacency() { return adj; }

    public List<Integer> getAdjacencySimple(int v) {
        List<Integer> list = new ArrayList<>();
        for (Edge e : adj.get(v)) list.add(e.to);
        return list;
    }

    public static class Edge {
        public final int to;
        public final int weight;
        public Edge(int to, int weight) { this.to = to; this.weight = weight; }
        @Override public String toString() { return String.format("(%d,w=%d)", to, weight); }
    }
}
