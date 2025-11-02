package graph;

import java.util.ArrayList;
import java.util.List;


public class Graph {
    public final int n;
    public final List<List<Edge>> adj;

    public Graph(int n) {
        this.n = n;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, long w) {
        adj.get(u).add(new Edge(u, v, w));
    }

    public static class Edge {
        public final int u;
        public final int v;
        public final long w;
        public Edge(int u, int v, long w) { this.u = u; this.v = v; this.w = w; }
        @Override public String toString() { return "(" + u + "->" + v + ":" + w + ")"; }
    }
}
