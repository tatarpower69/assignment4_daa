package graph.utils;



import com.fasterxml.jackson.databind.ObjectMapper;
import graph.Graph;
import graph.scc.KosarajuSCC;
import graph.scc.SCCResult;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GraphOutputGenerator {

    public static void main(String[] args) {
        String inputDir = "data/";

        String outputDir = "data/output/";

        File inFolder = new File(inputDir);
        if (!inFolder.exists()) {
            System.out.println(" Input folder not found: " + inputDir);
            return;
        }

        // Создаем output, если его нет
        File outFolder = new File(outputDir);
        if (!outFolder.exists()) outFolder.mkdirs();

        File[] files = inFolder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.out.println("⚠️ No input JSON files found in: " + inputDir);
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        for (File file : files) {
            try {
                // Загружаем граф
                GraphLoader.GraphData data = GraphLoader.loadGraph(file.getPath());
                Graph graph = data.graph;

                // Запускаем Kosaraju
                KosarajuSCC kosaraju = new KosarajuSCC(graph, new Metrics());
                SCCResult result = kosaraju.run();

                // Формируем JSON с результатом
                Map<String, Object> output = new HashMap<>();
                output.put("input_file", file.getName());
                output.put("components_count", result.getComponents().size());
                output.put("components", result.getComponents());

                // Сохраняем файл
                File outFile = new File(outputDir + file.getName());
                mapper.writerWithDefaultPrettyPrinter().writeValue(outFile, output);

                System.out.println("✅ Saved output: " + outFile.getPath());

            } catch (IOException e) {
                System.err.println("❌ Failed to process " + file.getName() + ": " + e.getMessage());
            }
        }
    }
}
