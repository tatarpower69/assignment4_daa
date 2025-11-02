package graph.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graph.Graph;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Simple JSON loader. Expects a JSON like:
 * {
 *   "n": 5,
 *   "edges": [ {"from":0,"to":1,"w":3}, {"from":1,"to":2,"w":4} ]
 * }
 *
 * Save files under /data and call GraphLoader.loadFromFile(path).
 */
public class GraphLoader {
    private static final ObjectMapper M = new ObjectMapper();

    public static Graph loadFromFile(String path) throws IOException {
        JsonNode root = M.readTree(new File(path));
        int n = root.path("n").asInt();
        Graph g = new Graph(n);
        JsonNode edges = root.path("edges");
        if (edges.isArray()) {
            Iterator<JsonNode> it = edges.elements();
            while (it.hasNext()) {
                JsonNode e = it.next();
                int from = e.path("from").asInt();
                int to = e.path("to").asInt();
                int w = e.has("w") ? e.path("w").asInt() : 1;
                g.addEdge(from, to, w);
            }
        }
        return g;
    }
}
