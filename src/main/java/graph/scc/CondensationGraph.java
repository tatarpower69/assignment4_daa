package graph.scc;

import graph.utils.Metrics;
import java.util.*;

/**
 * Класс строит конденсацию графа (сжатый DAG, где каждая вершина — это SCC).
 */
public class CondensationGraph {
    private final Map<Integer, List<Integer>> condensationAdj;
    private final Map<Integer, Integer> nodeToComponent;
    private final int componentCount;
    private final Metrics metrics;

    /**
     * Создает конденсацию по результатам SCC.
     *
     * @param originalAdj  исходный граф в виде списка смежности
     * @param sccResult    результат KosarajuSCC с компонентами
     * @param metrics      метрики для подсчета операций
     */
    public CondensationGraph(Map<Integer, List<int[]>> originalAdj, SCCResult sccResult, Metrics metrics) {
        this.metrics = metrics;
        this.nodeToComponent = new HashMap<>();

        // Построение соответствия: вершина -> компонент
        List<List<Integer>> components = sccResult.getComponents();
        for (int i = 0; i < components.size(); i++) {
            for (int v : components.get(i)) {
                nodeToComponent.put(v, i);
                metrics.incrementOperations();
            }
        }

        this.componentCount = components.size();
        this.condensationAdj = new HashMap<>();

        // Строим рёбра между компонентами (без повторов)
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

        // Убедимся, что каждая компонента есть в ключах
        for (int i = 0; i < componentCount; i++) {
            condensationAdj.computeIfAbsent(i, k -> new ArrayList<>());
        }
    }

    /** Возвращает список смежности DAG-конденсации */
    public Map<Integer, List<Integer>> getCondensationAdj() {
        return condensationAdj;
    }

    /** Возвращает количество компонент в DAG */
    public int getComponentCount() {
        return componentCount;
    }

    /** Возвращает отображение: вершина → компонент */
    public Map<Integer, Integer> getNodeToComponentMap() {
        return nodeToComponent;
    }

    /** Возвращает количество операций (через Metrics) */
    public long getOperationCount() {
        return metrics != null ? metrics.getOperationCount() : -1;
    }

    /** Печатает DAG-конденсацию */
    public void printCondensation() {
        System.out.println("Condensation Graph:");
        for (int comp : condensationAdj.keySet()) {
            System.out.println("Component " + comp + " -> " + condensationAdj.get(comp));
        }
    }
}
