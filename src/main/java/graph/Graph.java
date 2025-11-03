package graph;


import java.util.*;


public class Graph {


    public static class Edge {
        public final int to;
        public final int weight;


        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }


        public int getTo() { return to; }
        public int getWeight() { return weight; }


        @Override
        public String toString() {
            return "(" + to + ", w=" + weight + ")";
        }
    }


    private final int n;
    private final boolean directed;
    private final Map<Integer, List<Edge>> adjacency;
    private int source = 0;


    public Graph(int n) { this(n, true); }
    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adjacency = new HashMap<>();
        for (int i = 0; i < n; i++) adjacency.put(i, new ArrayList<>());
    }


    public void addEdge(int u, int v, int w) {
        adjacency.get(u).add(new Edge(v, w));
        if (!directed) adjacency.get(v).add(new Edge(u, w));
    }


    public List<Edge> getNeighbors(int u) {
        return adjacency.getOrDefault(u, Collections.emptyList());
    }


    public List<Edge> getAdjList(int u) { return getNeighbors(u); }


    public Map<Integer, List<Edge>> getAdjacency() { return adjacency; }


    public int size() { return n; }
    public int vertexCount() { return n; }


    public int edgeCount() {
        int sum = 0;
        for (List<Edge> list : adjacency.values()) sum += list.size();
        return directed ? sum : sum / 2;
    }


    public boolean isDirected() { return directed; }


    public void setSource(int s) { this.source = s; }
    public int getSource() { return source; }


    public Graph transpose() {
        Graph t = new Graph(n, directed);
        for (int u = 0; u < n; u++) {
            for (Edge e : getNeighbors(u)) t.addEdge(e.to, u, e.weight);
        }
        return t;
    }
}