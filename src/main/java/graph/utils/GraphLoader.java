package graph.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.google.gson.*;

public class GraphLoader {

    public static class Edge {
        public int u;
        public int v;
        public int w;
    }

    public static class GraphData {
        public boolean directed;
        public int n;
        public List<Edge> edges;
        public int source;
        public String weight_model;
    }

    /**
     * Load a graph from JSON file.
     * @param path Path to JSON file (e.g. "data/s1.json")
     * @return Loaded GraphData object
     */
    public static GraphData loadGraph(String path) {
        try (FileReader reader = new FileReader(path)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, GraphData.class);
        } catch (IOException e) {
            System.err.println("Error reading file: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public static Map<Integer, List<int[]>> toAdjList(GraphData data) {
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int i = 0; i < data.n; i++) {
            adj.put(i, new ArrayList<>());
        }
        for (Edge e : data.edges) {
            adj.get(e.u).add(new int[]{e.v, e.w});
            // if undirected support needed in the future

        }
        return adj;
    }

    /**
     * Smpl print.
     */
    public static void printGraph(GraphData data) {
        System.out.println("Directed: " + data.directed);
        System.out.println("Nodes: " + data.n);
        System.out.println("Source: " + data.source);
        System.out.println("Weight model: " + data.weight_model);
        System.out.println("Edges:");
        for (Edge e : data.edges) {
            System.out.printf("  %d -> %d (w=%d)%n", e.u, e.v, e.w);
        }
    }

    // Exmpl:
    public static void main(String[] args) {
        GraphData g = loadGraph("data/s1.json");
        if (g != null) {
            printGraph(g);
        }
    }
}
