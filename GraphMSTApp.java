﻿# Kruskal and Prism Gui
import javax.swing.*;
import java.awt.*;
import java.util.*;
class GraphMSTApp extends JFrame {
    private JTextField edgeInput, weightInput;
    private JButton kruskalButton, primButton, addEdgeButton;
    private JTextArea resultArea;
    private ArrayList<Edge> edges; 
    private Set<String> nodes; 
    public GraphMSTApp() {
        setTitle("Kruskal's and Prim's Algorithm GUI");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        edges = new ArrayList<>();
        nodes = new HashSet<>();
        JPanel inputPanel = new JPanel();
        edgeInput = new JTextField(5);
        weightInput = new JTextField(5);
        addEdgeButton = new JButton("Add Edge");
        kruskalButton = new JButton("Run Kruskal's");
        primButton = new JButton("Run Prim's");
        inputPanel.add(new JLabel("Edge (A-B):"));
        inputPanel.add(edgeInput);
        inputPanel.add(new JLabel("Weight:"));
        inputPanel.add(weightInput);
        inputPanel.add(addEdgeButton);
        inputPanel.add(kruskalButton);
        inputPanel.add(primButton);
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        addEdgeButton.addActionListener(e -> addEdge());
        kruskalButton.addActionListener(e -> runKruskal());
        primButton.addActionListener(e -> runPrim());
    }
    private void addEdge() {
        String edgeText = edgeInput.getText();
        String weightText = weightInput.getText();

        if (edgeText.isEmpty() || weightText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both edge and weight!");
            return;
        }

        String[] nodesArray = edgeText.split("-");
        if (nodesArray.length != 2) {
            JOptionPane.showMessageDialog(this, "Edge format should be A-B.");
            return;
        }

        try {
            int weight = Integer.parseInt(weightText);
            String node1 = nodesArray[0].trim();
            String node2 = nodesArray[1].trim();
            edges.add(new Edge(node1, node2, weight));
            nodes.add(node1);
            nodes.add(node2);

            resultArea.append("Added Edge: " + node1 + " - " + node2 + " (Weight: " + weight + ")\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Weight must be an integer!");
        }
    }
    // Kruskal's Algorithm
    private void runKruskal() {
        Collections.sort(edges); 
        UnionFind uf = new UnionFind(nodes.size());

        Map<String, Integer> nodeMap = new HashMap<>();
        int i = 0;
        for (String node : nodes) {
            nodeMap.put(node, i++);
        }
        int mstWeight = 0;
        ArrayList<Edge> mstEdges = new ArrayList<>();
        for (Edge edge : edges) {
            int u = nodeMap.get(edge.node1);
            int v = nodeMap.get(edge.node2);

            if (uf.find(u) != uf.find(v)) {
                uf.union(u, v);
                mstEdges.add(edge);
                mstWeight += edge.weight;
            }
        }
        displayMST("Kruskal's Algorithm", mstEdges, mstWeight);
    }
    private void runPrim() {
        if (nodes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No nodes in graph.");
            return;
        }
        PriorityQueue<Edge> pq = new PriorityQueue<>(edges);
        Set<String> mstSet = new HashSet<>();
        ArrayList<Edge> mstEdges = new ArrayList<>();
        int mstWeight = 0;
        String startNode = nodes.iterator().next();
        mstSet.add(startNode);
        while (mstSet.size() < nodes.size()) {
            Edge edge = pq.poll();
            if (edge == null) break;
            if (mstSet.contains(edge.node1) && mstSet.contains(edge.node2)) continue;
            mstEdges.add(edge);
            mstWeight += edge.weight;
            mstSet.add(edge.node1);
            mstSet.add(edge.node2);
        }
        displayMST("Prim's Algorithm", mstEdges, mstWeight);
    }
    private void displayMST(String algorithm, ArrayList<Edge> mstEdges, int mstWeight) {
        resultArea.append("\n" + algorithm + " MST:\n");
        for (Edge edge : mstEdges) {
            resultArea.append(edge.node1 + " - " + edge.node2 + " (Weight: " + edge.weight + ")\n");
        }
        resultArea.append("Total MST Weight: " + mstWeight + "\n\n");
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GraphMSTApp().setVisible(true));
    }
}
class Edge implements Comparable<Edge> {
    String node1, node2;
    int weight;
    public Edge(String node1, String node2, int weight) {
        this.node1 = node1;
        this.node2 = node2;
        this.weight = weight;
    }
    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}
class UnionFind {
    private int[] parent, rank;
    public UnionFind(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    public int find(int u) {
        if (parent[u] != u) {
            parent[u] = find(parent[u]);
        }
        return parent[u];
    }
    public void union(int u, int v) {
        int rootU = find(u);
        int rootV = find(v);
        if (rootU != rootV) {
            if (rank[rootU] > rank[rootV]) {
                parent[rootV] = rootU;
            } else if (rank[rootU] < rank[rootV]) {
                parent[rootU] = rootV;
            } else {
                parent[rootV] = rootU;
                rank[rootU]++;
            }
        }
    }
      
}
