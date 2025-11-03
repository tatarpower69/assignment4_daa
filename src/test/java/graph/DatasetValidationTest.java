package graph;

import graph.utils.GraphLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DatasetValidationTest {

    private static final String DATA_PATH = "data/";

    private static final String[][] FILES = {
            {"small", "s1.json"}, {"small", "s2.json"}, {"small", "s3.json"},
            {"medium", "m1.json"}, {"medium", "m2.json"}, {"medium", "m3.json"},
            {"large", "l1.json"}, {"large", "l2.json"}, {"large", "l3.json"}
    };

    @BeforeAll
    static void checkFilesExist() {
        for (String[] group : FILES) {
            String path = DATA_PATH + group[0] + "/" + group[1];
            File f = new File(path);
            assertTrue(f.exists(), "File not found: " + path);
        }
    }

    @Test
    @DisplayName("Validate JSON loading and basic graph invariants")
    void validateGraphStructureAndConnectivity() {
        for (String[] group : FILES) {
            String filePath = DATA_PATH + group[0] + "/" + group[1];
            GraphLoader.GraphData data = GraphLoader.loadGraph(filePath);
            Graph graph = data.graph;

            assertNotNull(graph, "GraphLoader returned null for " + filePath);
            assertTrue(graph.vertexCount() > 0, "Graph must have vertices: " + filePath);
            assertTrue(graph.edgeCount() >= 0, "Edge count must be non-negative: " + filePath);


            Set<Integer> unique = new HashSet<>();
            for (int i = 0; i < graph.vertexCount(); i++) {
                assertTrue(unique.add(i), "Duplicate vertex id=" + i + " in " + filePath);
            }


            for (int u = 0; u < graph.vertexCount(); u++) {
                for (Graph.Edge e : graph.getAdjList(u)) {
                    assertTrue(e.getTo() >= 0 && e.getTo() < graph.vertexCount(),
                            "Invalid destination " + e.getTo() + " in file " + filePath);
                }
            }


            boolean[] visited = new boolean[graph.vertexCount()];
            int src = data.source;
            if (src < 0 || src >= graph.vertexCount()) src = 0;
            dfs(graph, src, visited);

            int visitedCount = 0;
            for (boolean v : visited) if (v) visitedCount++;
            assertTrue(visitedCount > 0, "No vertex reachable from source in " + filePath);
        }
    }

    private void dfs(Graph g, int v, boolean[] visited) {
        if (visited[v]) return;
        visited[v] = true;
        for (Graph.Edge e : g.getAdjList(v)) {
            dfs(g, e.getTo(), visited);
        }
    }
}
