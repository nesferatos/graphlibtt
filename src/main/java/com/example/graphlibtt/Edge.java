package com.example.graphlibtt;

import java.util.Objects;

public class Edge<EDGE_T, VERTEX_T> {

    private final Graph graph;

    private final Vertex source;

    private final Vertex target;

    private final boolean directed;

    private final EDGE_T value;

    public Edge(Graph graph, Vertex<VERTEX_T> source, Vertex<VERTEX_T> target, boolean directed, EDGE_T value) {
        this.graph = graph;
        this.source = source;
        this.target = target;
        this.directed = directed;
        this.value = value;
    }

    public Graph getGraph() {
        return graph;
    }

    public Vertex<VERTEX_T> getSource() {
        return source;
    }

    public Vertex<VERTEX_T> getTarget() {
        return target;
    }

    public EDGE_T getValue() {
        return value;
    }

    public boolean isDirected() {
        return directed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (isDirected() != ((Edge) o).isDirected()) {
            return false;
        }
        if (directed) {
            return Objects.equals(getSource(), edge.getSource()) && Objects.equals(getTarget(), edge.getTarget());
        } else {
            return (Objects.equals(getSource(), edge.getSource()) && Objects.equals(getTarget(), edge.getTarget()) ||
                    Objects.equals(getSource(), edge.getTarget()) && Objects.equals(getTarget(), edge.getSource()));
        }
    }

    @Override
    public int hashCode() {
        if (directed) {
            return Objects.hash(getSource(), getTarget(), getValue());
        } else {
            Vertex v1;
            Vertex v2;
            int sourceHash = Objects.hash(getSource());
            int targetHash = Objects.hash(getTarget());
            if (sourceHash < targetHash) {
                v1 = getSource();
                v2 = getTarget();
            } else {
                v1 = getTarget();
                v2 = getSource();
            }
            return Objects.hash(v1, v2, getValue());
        }
    }

    @Override
    public String toString() {
        if (directed) {
            return String.format("%s--[%s]->%s", source, value, target);
        } else {
            return String.format("%s--[%s]--%s", source, value, target);
        }
    }
}
