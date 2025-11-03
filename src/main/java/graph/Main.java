package graph;

import graph.scc.KosarajuSCC;
import graph.scc.CondensationGraph;
import graph.scc.SCCResult;
import graph.topo.DFSTopoSort;
import graph.dagsp.DAGShortestPath;
import graph.dagsp.DAGLongestPath;
import graph.dagsp.PathResult;
import graph.utils.GraphLoader;
import graph.utils.Metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

    public static void main(String[] args) {

        List<String> datasets = (args.length > 0)
                ? List.of(args[0])
                : List.of("small", "medium", "large");

        for (String dataset : datasets) {
            processDataset(dataset);
        }
    }

    private static void processDataset(String dataset) {
        String folderPath = "data/" + dataset;
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Folder not found: " + folderPath);
            return;
        }

        List<Map<String, Object>> graphResults = new ArrayList<>();

        for (File file : Objects.requireNonNull(folder.listFiles((d, name) -> name.endsWith(".json")))) {
            System.out.println(" Processing graph: " + file.getName());
            Map<String, Object> result = processGraph(file.getPath());
            result.put("graph", file.getName().replace(".json",""));
            graphResults.add(result);
        }

        Map<String, Object> finalJson = new HashMap<>();
        finalJson.put("graphs", graphResults);
        finalJson.put("dataset", dataset);

        try {
            File outputDir = new File("output/" + dataset);
            if (!outputDir.exists()) outputDir.mkdirs();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(new File(outputDir, dataset + "_report.json"));
            gson.toJson(finalJson, writer);
            writer.close();
            System.out.println("Results saved to: " + outputDir.getPath() + "/" + dataset + "_report.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> processGraph(String path) {
        GraphLoader.GraphData data = GraphLoader.loadGraph(path);
        Graph g = data.graph;
        Metrics metrics = new Metrics();

        // SCC
        KosarajuSCC scc = new KosarajuSCC(g, metrics);
        SCCResult sccResult = scc.run();

        CondensationGraph cond = new CondensationGraph(convertToAdjMap(g), sccResult, metrics);
        cond.printCondensation();


        DFSTopoSort topo = new DFSTopoSort();
        List<Integer> topoOrder = topo.run(g, metrics);

        // DAG Shortest
        PathResult shortest = DAGShortestPath.shortestPath(g, data.source, topoOrder, metrics);

        // DAG Longest
        PathResult longest = DAGLongestPath.longestPath(g, data.source, topoOrder, metrics);

        // JSON
        Map<String, Object> graphJson = new HashMap<>();
        graphJson.put("scc_count", sccResult.size());

        // metrics
        Map<String, Object> metricsJson = new HashMap<>();
        metricsJson.put("DFS_Visits", metrics.getDfsVisits());
        metricsJson.put("Edge_Visits", metrics.getEdgeVisits());
        metricsJson.put("Relaxations", metrics.getRelaxations());
        metricsJson.put("Push_Ops", metrics.getPushOps());
        metricsJson.put("Pop_Ops", metrics.getPopOps());
        metricsJson.put("Total_Operations", metrics.getOperationCount());
        metricsJson.put("Elapsed_Time_ns", metrics.getElapsedTime());

        graphJson.put("metrics", Map.of(
                "Topological_Sort", metricsJson,
                "Kosaraju_SCC", metricsJson,
                "DAG_Longest", metricsJson,
                "DAG_Shortest", metricsJson
        ));

        graphJson.put("shortest_summary", summarizePath(shortest, false));
        graphJson.put("longest_summary", summarizePath(longest, true));

        return graphJson;
    }

    private static Map<String, Object> summarizePath(PathResult result, boolean longest) {
        double[] dist = result.getDist();
        int reachable = 0;
        double sum = 0;
        int bestNode = -1;
        double bestDist = longest ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        for (int i = 0; i < dist.length; i++) {
            if (!Double.isInfinite(dist[i])) {
                reachable++;
                sum += dist[i];
                if ((longest && dist[i] > bestDist) || (!longest && dist[i] < bestDist)) {
                    bestDist = dist[i];
                    bestNode = i;
                }
            }
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("average_distance", (reachable == 0) ? 0 : sum / reachable);
        summary.put("reachable_nodes", reachable);
        summary.put("best_path", List.of(bestNode));
        summary.put("best_distance", bestDist);

        return summary;
    }

    private static Map<Integer, List<int[]>> convertToAdjMap(Graph g) {
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int u = 0; u < g.size(); u++) {
            List<int[]> list = new ArrayList<>();
            for (Graph.Edge e : g.getNeighbors(u)) list.add(new int[]{e.getTo(), e.getWeight()});
            adj.put(u, list);
        }
        return adj;
    }
}
