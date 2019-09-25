package com.example.graphlibtt;

import java.util.*;
import java.util.function.Function;

public class Graph<VERTEX_T, EDGE_T> {

    private HashMap<Vertex<VERTEX_T>, VertexShell<EDGE_T, VERTEX_T>> vertices = new HashMap<>();

    public void addVertex(VERTEX_T elem) {
        Vertex<VERTEX_T> nVertex = new Vertex<>(this, elem);
        if (!vertices.containsKey(nVertex)) {
            vertices.put(nVertex, new VertexShell());
        }
    }

    public void addEdge(VERTEX_T source, VERTEX_T target, boolean directed, EDGE_T value) {
        Edge<EDGE_T, VERTEX_T> edge = createEdge(source, target, directed, value);
        assertVertexExists(edge.getSource());
        assertVertexExists(edge.getTarget());

        if (directed) {
            vertices.get(edge.getSource()).getOutputEdges().add(edge);
            vertices.get(edge.getTarget()).getInputEdges().add(edge);
        } else {
            vertices.get(edge.getSource()).getUndirectedEdges().add(edge);
            vertices.get(edge.getTarget()).getUndirectedEdges().add(edge);
        }
    }

    private LinkedList<Edge<EDGE_T, VERTEX_T>> findPath(Set<Vertex<VERTEX_T>> visited,
                          Vertex<VERTEX_T> from,
                          Vertex<VERTEX_T> to,
                          Function<EDGE_T, Boolean> matchEdge) {
        if (!visited.contains(from)) {
            visited.add(from);
            for (Edge<EDGE_T, VERTEX_T> edge : vertices.get(from).getOutputEdges()) {
                if (matchEdge.apply(edge.getValue())) {
                    if (edge.getTarget().equals(to)) {
                        LinkedList<Edge<EDGE_T, VERTEX_T>> path = new LinkedList<>();
                        path.addLast(edge);
                        return path;
                    } else {
                        LinkedList<Edge<EDGE_T, VERTEX_T>> path = findPath(visited, edge.getTarget(), to, matchEdge);
                        if (!path.isEmpty()) {
                            path.addFirst(edge);
                            return path;
                        }
                    }
                }
            }

            for (Edge<EDGE_T, VERTEX_T> edge : vertices.get(from).getUndirectedEdges()) {
                if (matchEdge.apply(edge.getValue())) {
                    if (edge.getTarget().equals(to) || edge.getSource().equals(to)) {
                        LinkedList<Edge<EDGE_T, VERTEX_T>> path = new LinkedList<>();
                        path.addLast(edge);
                        return path;
                    } else {
                        LinkedList<Edge<EDGE_T, VERTEX_T>> path = findPath(visited, edge.getTarget(), to, matchEdge);
                        if (path.isEmpty()) {
                            path = findPath(visited, edge.getSource(), to, matchEdge);
                        }
                        if (!path.isEmpty()) {
                            path.addFirst(edge);
                            return path;
                        }
                    }
                }
            }
        }
        return new LinkedList<>();
    }

    public List<Edge<EDGE_T, VERTEX_T>> getPath(VERTEX_T from, VERTEX_T to, Function<EDGE_T, Boolean> matchEdge) {
        Vertex<VERTEX_T> fromVertex = new Vertex<>(this, from);
        assertVertexExists(fromVertex);
        Vertex<VERTEX_T> toVertex = new Vertex<>(this, to);
        assertVertexExists(toVertex);
        Set<Vertex<VERTEX_T>> visited = new HashSet<>();
        return findPath(visited, fromVertex, toVertex, matchEdge);
    }

    public boolean hasEdge(VERTEX_T source, VERTEX_T target, boolean directed, EDGE_T value) {
        Edge<EDGE_T, VERTEX_T> edge = createEdge(source, target, directed, value);
        if (directed) {
            return vertices.get(edge.getSource()).getOutputEdges().contains(edge);
        } else {
            return vertices.get(edge.getSource()).getUndirectedEdges().contains(edge);
        }
    }

    private Edge<EDGE_T, VERTEX_T> createEdge(VERTEX_T source, VERTEX_T target, boolean directed, EDGE_T value) {
        return new Edge<>(this,
                new Vertex<>(this, source),
                new Vertex<>(this, target),
                directed,
                value);
    }

    private void assertVertexExists(Vertex<VERTEX_T> vertex) {
        if (!vertices.containsKey(vertex)) {
            throw new RuntimeException(vertex.getValue() + " vertex doesn't exists");
        }
    }

}
