package graph.scc;

import graph.utils.Metrics;
import java.util.*;


public class CondensationGraph {
    private final Map<Integer, List<Integer>> condensationAdj;
    private final Map<Integer, Integer> nodeToComponent;
    private final int componentCount;
    private final Metrics metrics;


    public CondensationGraph(Map<Integer, List<int[]>> originalAdj, SCCResult sccResult, Metrics metrics) {
        this.metrics = metrics;
        this.nodeToComponent = new HashMap<>();

        List<List<Integer>> components = sccResult.getComponents();
        for (int i = 0; i < components.size(); i++) {
            for (int v : components.get(i)) {
                nodeToComponent.put(v, i);
                metrics.incrementOperations();
            }
        }

        this.componentCount = components.size();
        this.condensationAdj = new HashMap<>();

        Set<String> edgeSet = new HashSet<>();
        for (Map.Entry<Integer, List<int[]>> entry : originalAdj.entrySet()) {
            int u = entry.getKey();
            int cu = nodeToComponent.get(u);
            for (int[] edge : entry.getValue()) {
                int v = edge[0];
                int cv = nodeToComponent.get(v);
                metrics.incrementOperations();

                if (cu != cv) {
                    String edgeId = cu + "->" + cv;
                    if (edgeSet.add(edgeId)) {
                        condensationAdj.computeIfAbsent(cu, k -> new ArrayList<>()).add(cv);
                    }
                }
            }
        }

        for (int i = 0; i < componentCount; i++) {
            condensationAdj.computeIfAbsent(i, k -> new ArrayList<>());
        }
    }

    public Map<Integer, List<Integer>> getCondensationAdj() {
        return condensationAdj;
    }

    public int getComponentCount() {
        return componentCount;
    }

    public Map<Integer, Integer> getNodeToComponentMap() {
        return nodeToComponent;
    }

    public long getOperationCount() {
        return metrics != null ? metrics.getOperationCount() : -1;
    }

    public void printCondensation() {
        System.out.println("Condensation Graph:");
        for (int comp : condensationAdj.keySet()) {
            System.out.println("Component " + comp + " -> " + condensationAdj.get(comp));
        }
    }
}
