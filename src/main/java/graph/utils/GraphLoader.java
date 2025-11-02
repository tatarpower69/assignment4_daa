package graph.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import graph.Graph;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class GraphLoader {
    public static class EdgeJson { public int u; public int v; public long w; }
    public static class GraphJson {
        public boolean directed;
        public int n;
        public List<EdgeJson> edges;
        public Integer source;
        @SerializedName("weight_model")
        public String weightModel;
    }

    private static final Gson GSON = new Gson();

    public static GraphJson loadJson(String path) throws Exception {
        try (FileReader fr = new FileReader(path)) {
            return GSON.fromJson(fr, GraphJson.class);
        }
    }

    public static Graph toGraph(GraphJson gjson) {
        Graph g = new Graph(gjson.n);
        if (gjson.edges != null) {
            for (EdgeJson e : gjson.edges) g.addEdge(e.u, e.v, e.w);
        }
        return g;
    }
}
