# Assignment 4 – Smart City / Smart Campus Scheduling

Student: Ilnur Saipiyev
## Goal
Optimize the planning and routing of urban service tasks (cleaning, repairs) using SCC and DAG algorithms (Topological Sorting, Shortest Paths) to improve the speed and quality of service.

---

## Scenario
You receive datasets representing city-service tasks (street cleaning, repairs, camera/sensor maintenance) and internal analytics subtasks.
- Some dependencies are **cyclic** – detect & compress them.
- Others are **acyclic** – plan optimally.
- Separate subtasks use standard dynamic programming (DP) patterns.

---

## Implemented Tasks

### 1. Graph Tasks

#### 1.1 Strongly Connected Components (SCC)
- **Algorithm:** Kosaraju's algorithm
- **Input:** Directed dependency graph (`tasks.json`)
- **Output:**
    - List of strongly connected components (each as a list of vertices)
    - Sizes of SCCs
    - Condensation graph (DAG of components)

#### 1.2 Topological Sort
- **Algorithm:** DFS-based topological sort on the condensation DAG
- **Output:**
    - Valid topological order of SCC components
    - Derived order of original tasks after SCC compression

#### 1.3 Shortest Paths in a DAG
- **Weight model:** **Edge weights** are used for shortest/longest paths
- **Implemented:**
    - Single-source shortest paths on the DAG
    - Longest path (critical path) via max-DP over topological order
- **Output:**
    - Critical path (longest) and its length
    - Shortest distances from a given source
    - Reconstructed one optimal path

---

## 2. Dataset Generation

All datasets are stored under `/data/`:

| Category | Nodes (n) | Description | Variants |
|----------|------------|------------|---------|
| Small    | 6–10       | Simple cases, 1–2 cycles or pure DAG | 3 |
| Medium   | 10–20      | Mixed structures, several SCCs       | 3 |
| Large    | 20–50      | Performance & timing tests           | 3 |

**Notes:**
- Different density levels (sparse vs dense) included
- Both cyclic and acyclic examples
- At least one graph with multiple SCCs per category

---

## 3. Instrumentation

- **Metrics Interface:** Operation counters + timing
- **Timing:** `System.nanoTime()`
- **Counters:**
    - DFS visits / edges (SCC)
    - Pushes / pops (Kahn for topological sort)
    - Relaxations (DAG shortest/longest paths)

---

## 4. Code Structure

```bash
src/main/java/graph
├── dagsp # DAG shortest/longest path classes
├── scc # Kosaraju/Tarjan SCC + CondensationGraph
├── topo # Topological sort
├── utils # GraphLoader, Metrics
└── Main.java # Entry point for dataset processing
```

## 5. Run Instructions

1. Clone the repository:
   ```bash
   git clone <https://github.com/tatarpower69/assignment4_daa.git>
   cd assignment4_daa
   ```

## Package Structure   
```bash
|   .gitignore
|   pom.xml
|   README.md
|   structure.txt
|   
+---.idea
+---.mvn
+---data
|   +---large
|   |       l1.json
|   |       l2.json
|   |       l3.json
|   |       
|   +---medium
|   |       m1.json
|   |       m2.json
|   |       m3.json
|   |       
|   \---small
|           s1.json
|           s2.json
|           s3.json
|           
+---output
|   +---large
|   |       large_report.json
|   |       
|   +---medium
|   |       medium_report.json
|   |       
|   \---small
|           small_report.json
|           
+---src
|   +---main
|   |   +---java
|   |   |   \---graph
|   |   |       |   Graph.java
|   |   |       |   Main.java
|   |   |       |   
|   |   |       +---dagsp
|   |   |       |       DAGLongestPath.java
|   |   |       |       DAGShortestPath.java
|   |   |       |       PathResult.java
|   |   |       |       
|   |   |       +---scc
|   |   |       |       CondensationGraph.java
|   |   |       |       KosarajuSCC.java
|   |   |       |       SCCResult.java
|   |   |       |       
|   |   |       +---topo
|   |   |       |       DFSTopoSort.java
|   |   |       |       KahnTopoSort.java
|   |   |       |       
|   |   |       \---utils
|   |   |               GraphLoader.java
|   |   |               GraphOutputGenerator.java
|   |   |               Metrics.java
|   |   |               TimerUtil.java
|   |   |               
|   |   \---resources
|   \---test
|       \---java
|           \---graph
                   DAGSPTest.java
                   DatasetValidationTest.java
                   SCCAlgorithmTest.java
                   TopoSortTest.java
                     
```
### 1.  Dataset Overview

### Example dataset format (data/small.json)
```bash
{
  "directed": true,
  "n": 6,
  "edges": [
    {"u": 0, "v": 1, "w": 3},
    {"u": 0, "v": 2, "w": 2},
    {"u": 1, "v": 3, "w": 4},
    {"u": 2, "v": 3, "w": 1},
    {"u": 3, "v": 4, "w": 3},
    {"u": 4, "v": 5, "w": 2}
  ],
  "source": 0,
  "weight_model": "edge"
}
```

The analysis covers three datasets, each containing multiple graphs.

| Dataset | Description |
| :--- | :--- |
| **Small** | Contains 3 small-scale graphs. |
| **Medium** | Contains 3 medium-scale graphs. |
| **Large** | Contains 3 large-scale graphs. |


### Small Datasets

| Dataset  | Directed | Vertices (n) | Edges (m) | Source Node | Weight Model | Notes |
|----------|---------|--------------|-----------|-------------|--------------|-------|
| small1   | Yes     | 6            | 6         | 0           | edge         | Simple acyclic chain |
| small2   | Yes     | 7            | 7         | 1           | edge         | Contains a small cycle |
| small3   | Yes     | 8            | 9         | 2           | edge         | Disconnected DAG with multiple branches |

**Total:** 3 datasets, 21 vertices, 22 edges

---

### Medium Datasets

| Dataset  | Directed | Vertices (n) | Edges (m) | Source Node | Weight Model | Notes |
|----------|---------|--------------|-----------|-------------|--------------|-------|
| medium1  | Yes     | 12           | 12        | 3           | edge         | Several small cycles and linear chains |
| medium2  | Yes     | 15           | 14        | 5           | edge         | Mixed cyclic and acyclic subgraphs |
| medium3  | Yes     | 14           | 14        | 6           | edge         | Contains cycles and linear chains |

**Total:** 3 datasets, 41 vertices, 40 edges

---

### Large Datasets

| Dataset  | Directed | Vertices (n) | Edges (m) | Source Node | Weight Model | Notes |
|----------|---------|--------------|-----------|-------------|--------------|-------|
| large1   | Yes     | 25           | 24        | 0           | edge         | Mostly linear with few cycles |
| large2   | Yes     | 30           | 29        | 4           | edge         | Multiple cycles and disconnected subgraphs |
| large3   | Yes     | 40           | 40        | 10          | edge         | Large sparse DAG with few small cycles |

**Total:** 3 datasets, 95 vertices, 93 edges



### 2. Analysis Results: Small Dataset

**File:** `small.json` | **Graphs:** 3

| Graph ID | Vertices | SCC Count | Shortest Path Avg | Longest Path Avg | Reachable Nodes | Best Shortest Path | Best Longest Path |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **s1** | 6 | 6 | 3.6667 | 5.6667 | 6 | 0 | 5 |
| **s2** | 7 | 5 | 5.0000 | 5.0000 | 7 | 1 | 6 |
| **s3** | **8** | 8 | 3.0000 | 3.0000 | 3 | 2 | 7 |

**Summary for Small:**
Summary for Small:Graphs contain 6–8 vertices with SCC counts ranging from 5–8. The average DAG shortest and longest paths are small (Avg $3.78$ and $4.56$ respectively), reflecting limited overall connectivity in the condensation graph. SCC detection and topological sorting completed efficiently.
---

### 3. Analysis Results: Medium Dataset

**File:** `medium.json` | **Graphs:** 3

| Graph ID | Vertices | SCC Count | Shortest Path Avg | Longest Path Avg | Reachable Nodes | Best Shortest Path | Best Longest Path |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **m1** | 12 | 8 | 1.6667 | 3.6667 | 3 | 3 | 3 |
| **m2** | 15 | 15 | 2.2500 | 2.2500 | 4 | 5 | 14 |
| **m3** | 14 | 8 | 2.3333 | 4.3333 | 3 | 6 | 6 |

**Summary for Medium:**
Summary for Medium: Medium graphs contain 12–15 vertices. SCC counts range from 8–15, indicating increased cyclic structure compared to the small dataset. DAG shortest and longest paths remain low on average, confirming the efficiency and scalability of the DAG-based pathfinding approach.
---

### 4. Analysis Results: Large Dataset

**File:** `large.json` | **Graphs:** 3

| Graph ID | Vertices | SCC Count | Shortest Path Avg | Longest Path Avg | Reachable Nodes | Best Shortest Path | Best Longest Path |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **l1** | 25 | 25 | 28.0800 | 28.0800 | 25 | 0 | 24 |
| **l2** | 30 | 20 | 2.6667 | 2.6667 | 3 | 4 | 3 |
| **l3** | 40 | 40 | 8.9000 | 8.9000 | 10 | 10 | 37 |

**Summary for Large:**
Summary for Large: Large graphs contain 25–40 vertices with high SCC counts of 20–40, reflecting denser and more complex cyclic structures. The pathfinding distances show a clear increase in Graph l1 (which is essentially a long path structure), demonstrating that DAG algorithms scale efficiently even with large component counts.
---

### 5. Overall Dataset Summary (Averages)

| Dataset | \# Graphs | Avg Vertices | Avg SCCs | Avg Shortest Distance | Avg Longest Distance | Avg Reachable Nodes |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Small** | 3 | 7.0 | 6.3 | 3.7778 | 4.5556 | 5.3 |
| **Medium** | 3 | 13.7 | 10.3 | 2.0833 | 3.4167 | 3.3 |
| **Large** | 3 | 31.7 | 28.3 | 13.2200 | 13.2200 | 12.7 |

##  Graph Analysis Results

This document summarizes the structural and pathfinding analysis conducted on three distinct datasets: **Small**, **Medium**, and **Large**. The primary algorithms applied include: **Strongly Connected Components (SCC)** detection, **Condensation**, **Topological Sorting**, and **DAG-based Shortest/Longest Path** calculation.

* **SCCs (Strongly Connected Components):** Count of strongly connected components.
* **Condensation:** Transformation of the original graph into a Directed Acyclic Graph (DAG) of its SCCs.
* **Topological Sorting:** Ordering of vertices in the condensation graph.
* **DAG Shortest/Longest Paths:** Path calculation on the condensation graph.



#### Key Observations:

* **Complexity:** **SCC counts grow with graph size**, indicating more complex cyclic structures.
* **Path Scaling:** Average shortest and longest paths increase with graph size, though the maximum distance is often dictated by single-path graphs.
* **Efficiency:** **DAG algorithms remain highly efficient** across all datasets, successfully reducing complex graphs into simpler acyclic structures for pathfinding.
* **Connectivity:** Reachable nodes reflect graph structure and connectivity within the condensed graphs.

---
### Example dataset format of output (data/small.json)
```bash
"graphs": [
    {
      "longest_summary": {
        "average_distance": 5.666666666666667,
        "best_path": [
          5
        ],
        "best_distance": 12.0,
        "reachable_nodes": 6
      },
      "metrics": {
        "DAG_Longest": {
          "Total_Operations": 12,
          "Push_Ops": 0,
          "Pop_Ops": 0,
          "Edge_Visits": 18,
          "DFS_Visits": 18,
          "Relaxations": 12,
          "Elapsed_Time_ns": 0
        },
        "Topological_Sort": {
          "Total_Operations": 12,
          "Push_Ops": 0,
          "Pop_Ops": 0,
          "Edge_Visits": 18,
          "DFS_Visits": 18,
          "Relaxations": 12,
          "Elapsed_Time_ns": 0
        },
        "DAG_Shortest": {
          "Total_Operations": 12,
          "Push_Ops": 0,
          "Pop_Ops": 0,
          "Edge_Visits": 18,
          "DFS_Visits": 18,
          "Relaxations": 12,
          "Elapsed_Time_ns": 0
        },
        "Kosaraju_SCC": {
          "Total_Operations": 12,
          "Push_Ops": 0,
          "Pop_Ops": 0,
          "Edge_Visits": 18,
          "DFS_Visits": 18,
          "Relaxations": 12,
          "Elapsed_Time_ns": 0
        }
      },
      "shortest_summary": {
        "average_distance": 3.6666666666666665,
        "best_path": [
          0
        ],
        "best_distance": 0.0,
        "reachable_nodes": 6
      },
      "scc_count": 6,
      "graph": "s1"
    },
```
---
## 💡 Conclusions

The analysis successfully validates the **robustness and efficiency** of the implemented graph algorithms in modeling and solving complex dependency problems across various graph scales.

### Key Algorithmic Performance Takeaways:

* **SCC Detection & Condensation:** The process accurately identifies **Strongly Connected Components (SCCs)** in all datasets. This is crucial as it simplifies complex cyclic dependencies, effectively transforming the original graph into a **Directed Acyclic Graph (DAG)** for path analysis.
* **Topological Sorting:** Consistent topological ordering ensures a **deterministic and valid sequence of execution**. This functionality is fundamental for **task scheduling and dependency resolution** in real-world Smart City / Smart Campus systems.
* **Shortest Path (DAG-Based):** The computation of minimal distances is **highly efficient**. The average shortest path distances reflect graph complexity:
    * **Small:** 3.67–5.0
    * **Medium:** 1.67–2.33
    * **Large:** 2.67–28.08
* **Longest Path (Critical Path):** Longest path calculation effectively identifies **bottlenecks and maximum project durations**, which is vital for critical path analysis:
    * **Small:** 3.0–5.67
    * **Medium:** 2.25–4.33
    * **Large:** 2.67–28.08
* **DAG DP Efficiency:** Both shortest and longest path computations utilize dynamic programming over the DAG, demonstrating **excellent performance and scalability**. Computation remains fast even for large graphs (up to 40 vertices), while SCC detection scales linearly with graph size ($O(|V| + |E|)$).

### Practical Applications:

The validated implementation confirms that these graph algorithms can serve as a core component for:

* **Efficient Routing of Services** (e.g., waste collection, security patrols).
* **Predictive Maintenance Scheduling** and optimization of repair tasks.
* **Dependency Management** and optimizing complex operational workflows in Smart City / Smart Campus environments.