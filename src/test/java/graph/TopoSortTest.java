package graph;

import graph.topo.DFSTopoSort;
import graph.topo.KahnTopoSort;
import graph.utils.GraphLoader;
import graph.utils.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TopoSortTest {

    @Test
    public void testTopoSortsOnDAGs() throws Exception {
        List<String> dags = Arrays.asList(
                "data/small/s1.json", "data/small/s3.json",
                "data/medium/m2.json", "data/large/l1.json", "data/large/l3.json"
        );

        for (String path : dags) {
            GraphLoader.GraphData data = GraphLoader.loadGraph(path);
            Metrics metrics = new Metrics();

            DFSTopoSort dfs = new DFSTopoSort();
            List<Integer> dfsOrder = dfs.run(data.graph, metrics);

            KahnTopoSort kahn = new KahnTopoSort();
            List<Integer> kahnOrder = kahn.run(data.graph, metrics);

            assertNotNull(dfsOrder);
            assertNotNull(kahnOrder);
            assertEquals(data.graph.size(), dfsOrder.size(), "DFS topo must include all nodes");
            assertEquals(data.graph.size(), kahnOrder.size(), "Kahn topo must include all nodes");
        }
    }

    @Test
    public void testTopoSortOnTrivialGraph() {
        Graph g = new Graph(1, true);
        Metrics m = new Metrics();
        DFSTopoSort dfs = new DFSTopoSort();
        List<Integer> order = dfs.run(g, m);
        assertEquals(List.of(0), order, "Single node topo order should be itself");
    }
}
