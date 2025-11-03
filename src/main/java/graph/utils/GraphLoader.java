package graph.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graph.Graph;

import java.io.File;
import java.io.IOException;

/**
 * Loads JSON dataset into Graph + preserves original fields for tests.
 */
public class GraphLoader {

    public static class GraphData {
        public Graph graph;
        public int source;
        public String weightModel;

        // compatibility fields expected by some tests
        public int n;
        public JsonNode edges;

        public GraphData(Graph graph, int source, String weightModel, int n, JsonNode edges) {
            this.graph = graph;
            this.source = source;
            this.weightModel = weightModel;
            this.n = n;
            this.edges = edges;
        }
    }

    public static GraphData loadGraph(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(path));

            boolean directed = root.get("directed").asBoolean();
            int n = root.get("n").asInt();
            int source = root.has("source") ? root.get("source").asInt() : 0;
            String weightModel = root.has("weight_model") ? root.get("weight_model").asText() : "edge";

            Graph g = new Graph(n, directed);

            JsonNode edges = root.get("edges");
            if (edges != null && edges.isArray()) {
                for (JsonNode e : edges) {
                    int u = e.get("u").asInt();
                    int v = e.get("v").asInt();
                    int w = e.has("w") ? e.get("w").asInt() : 1;
                    g.addEdge(u, v, w);
                }
            }

            g.setSource(source);
            return new GraphData(g, source, weightModel, n, edges);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load graph from " + path, ex);
        }
    }
}
