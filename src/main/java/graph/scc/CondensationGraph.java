package graph.scc;

import graph.Graph;
import java.util.*;

/**
 * Builds condensation graph (DAG) from original Graph and SCC partition.
 * Each SCC becomes a node in the condensation DAG.
 */
public class CondensationGraph {

    /**
     * Build adjacency list of condensation DAG: list of lists of ints (component indices).
     * No parallel edges (uses sets).
     *
     * @param original input graph
     * @param sccs list of components (each is list of node ids)
     * @return adjacency of condensation graph (size = sccs.size())
     */
    public static List<List<Integer>> buildCondensation(Graph original, List<List<Integer>> sccs) {
        int m = sccs.size();
        Map<Integer, Integer> nodeToComp = new HashMap<>();
        for (int i = 0; i < m; i++) {
            for (int v : sccs.get(i)) nodeToComp.put(v, i);
        }

        List<Set<Integer>> sets = new ArrayList<>(m);
        for (int i = 0; i < m; i++) sets.add(new HashSet<>());

        int n = original.size();
        for (int v = 0; v < n; v++) {
            int a = nodeToComp.get(v);
            for (Graph.Edge e : original.getAdjacency().get(v)) {
                int b = nodeToComp.get(e.to);
                if (a != b) sets.get(a).add(b);
            }
        }

        List<List<Integer>> dag = new ArrayList<>();
        for (Set<Integer> s : sets) dag.add(new ArrayList<>(s));
        return dag;
    }
}
