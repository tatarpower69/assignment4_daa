package graph;

import graph.dagsp.DAGShortestPath;
import graph.dagsp.PathResult;
import graph.topo.DFSTopoSort;
import graph.utils.GraphLoader;
import graph.utils.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DAGSPTest {

    @Test
    public void testShortestPathOnAllDAGs() throws Exception {
        List<String> dags = Arrays.asList(
                "data/small/s1.json", "data/small/s3.json",
                "data/medium/m2.json", "data/large/l1.json", "data/large/l3.json"
        );

        for (String path : dags) {
            GraphLoader.GraphData data = GraphLoader.loadGraph(path);
            Metrics metrics = new Metrics();

            DFSTopoSort topo = new DFSTopoSort();
            List<Integer> order = topo.run(data.graph, metrics);
            PathResult result = DAGShortestPath.shortestPath(data.graph, data.source, order, metrics);

            assertNotNull(result, "PathResult must not be null");
            assertEquals(data.graph.size(), result.getDistances().length, "Distance array size mismatch");
            assertTrue(metrics.getRelaxations() >= 0, "Relaxations counter should be present");

            System.out.println(" DAG ShortestPath OK: " + path);
        }
    }

    @Test
    public void testShortestPathOnSingleNode() {
        Graph g = new Graph(1, true);
        Metrics m = new Metrics();
        List<Integer> topoOrder = List.of(0);
        PathResult r = DAGShortestPath.shortestPath(g, 0, topoOrder, m);

        assertEquals(0.0, r.getDistances()[0], "Distance to itself must be 0");
    }
}
