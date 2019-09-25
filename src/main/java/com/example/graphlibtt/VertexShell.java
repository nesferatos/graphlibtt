package com.example.graphlibtt;

import java.util.HashSet;
import java.util.Set;

class VertexShell<EDGE_T, VERTEX_T> {

    private final Set<Edge<EDGE_T, VERTEX_T>> inputEdges = new HashSet<>();
    private final Set<Edge<EDGE_T, VERTEX_T>> outputEdges = new HashSet<>();

    private final Set<Edge<EDGE_T, VERTEX_T>> undirectedEdges = new HashSet<>();

    public Set<Edge<EDGE_T, VERTEX_T>> getInputEdges() {
        return inputEdges;
    }

    public Set<Edge<EDGE_T, VERTEX_T>> getOutputEdges() {
        return outputEdges;
    }

    public Set<Edge<EDGE_T, VERTEX_T>> getUndirectedEdges() {
        return undirectedEdges;
    }

}
