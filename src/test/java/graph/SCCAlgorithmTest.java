package graph;

import graph.scc.CondensationGraph;
import graph.scc.KosarajuSCC;
import graph.scc.SCCResult;
import graph.utils.GraphLoader;
import graph.utils.Metrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SCCAlgorithmTest {

    private static final String TEST_GRAPH_PATH = "data/small/s1.json";

    @Test
    @DisplayName("Kosaraju SCC correctly finds components")
    void testKosarajuSCC() {
        var data = GraphLoader.loadGraph(TEST_GRAPH_PATH);
        Graph graph = data.graph;
        Metrics metrics = new Metrics();

        KosarajuSCC scc = new KosarajuSCC(graph, metrics);
        SCCResult result = scc.run();

        assertNotNull(result.getComponents(), "SCC result components are null");
        assertTrue(result.getComponents().size() > 0, "SCC found zero components");
        assertTrue(result.getComponents().stream().mapToInt(java.util.List::size).sum() == graph.vertexCount(),
                "SCC components don't cover all vertices");
    }

    @Test
    @DisplayName("Condensation graph builds without errors")
    void testCondensationGraph() {
        var data = GraphLoader.loadGraph(TEST_GRAPH_PATH);
        Graph graph = data.graph;
        Metrics metrics = new Metrics();

        KosarajuSCC scc = new KosarajuSCC(graph, metrics);
        SCCResult sccResult = scc.run();

        // строим конденсацию
        CondensationGraph condensation = new CondensationGraph(
                convertToSimpleAdj(graph),
                sccResult,
                metrics
        );

        assertNotNull(condensation.getCondensationAdj());
        assertTrue(condensation.getComponentCount() > 0);
        condensation.printCondensation();
    }

    /** Упрощаем структуру смежности до Map<Integer, List<int[]>> для совместимости с CondensationGraph */
    private java.util.Map<Integer, java.util.List<int[]>> convertToSimpleAdj(Graph g) {
        var map = new java.util.HashMap<Integer, java.util.List<int[]>>();
        for (int u = 0; u < g.vertexCount(); u++) {
            var list = new java.util.ArrayList<int[]>();
            for (Graph.Edge e : g.getAdjList(u)) {
                list.add(new int[]{e.getTo(), e.getWeight()});
            }
            map.put(u, list);
        }
        return map;
    }
}
