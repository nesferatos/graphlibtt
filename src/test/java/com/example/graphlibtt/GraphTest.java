package com.example.graphlibtt;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    private static Logger logger = LoggerFactory.getLogger(GraphTest.class);

    @Test
    void edges() {
        Graph<String, String> graph = new Graph();

        Vertex<String> v1 = new Vertex<>(graph, "1");
        Vertex<String> v2 = new Vertex<>(graph, "2");
        Vertex<String> v3 = new Vertex<>(graph, "3");

        Edge<String, String> e1 = new Edge<>(graph, v1, v2, true, "ref");
        Edge<String, String> e2 = new Edge<>(graph, v1, v2, true, "ref");
        Edge<String, String> e3 = new Edge<>(graph, v2, v1, true, "ref");

        Edge<String, String> e4 = new Edge<>(graph, v1, v2, false, "ref");
        Edge<String, String> e5 = new Edge<>(graph, v2, v1, false, "ref");

        Edge<String, String> e6 = new Edge<>(graph, v1, v3, false, "ref");

        Edge<String, String> e7 = new Edge<>(graph, v1, v2, false, "contains");

        assertEquals(e1, e2);
        assertEquals(e2, e1);

        assertNotEquals(e1, e3);
        assertNotEquals(e1, e4);

        assertEquals(e4, e5);
        assertEquals(e5, e4);

        assertNotEquals(e4, e6);

        Set<Edge> edges = new HashSet<>();
        edges.add(e1);
        edges.add(e2);
        edges.add(e3);
        edges.add(e4);


        assertTrue(edges.contains(e5));
        assertFalse(edges.contains(e6));
        assertFalse(edges.contains(e7));

    }

    @Test
    void graph() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("/");
        graph.addVertex("/etc");
        graph.addVertex("/bin");
        graph.addVertex("/home");
        graph.addVertex("/usr");
        graph.addVertex("/home/user");
        graph.addVertex("/home/user/.m2");
        graph.addVertex("/home/user/binref");
        graph.addVertex("/home/user/src");
        graph.addVertex("/home/user/src/site");
        graph.addVertex("/home/user/src/site/contacts");
        graph.addVertex("/var");
        graph.addVertex("/var/www");
        graph.addVertex("/var/www/site.lk");

        graph.addEdge("/", "/etc", true, "contains");
//        graph.addEdge("/", "/bin", true, "contains");
        graph.addEdge("/", "/home", true, "contains");
        graph.addEdge("/", "/usr", true, "contains");
        graph.addEdge("/", "/var", true, "contains");
        graph.addEdge("/var", "/var/www", true, "contains");
        graph.addEdge("/var/www", "/var/www/site.lk", true, "contains");
        graph.addEdge("/home", "/home/user", true, "contains");
        graph.addEdge("/home/user", "/home/user/.m2", true, "contains");
        graph.addEdge("/home/user", "/home/user/binref", true, "contains");
        graph.addEdge("/home/user", "/home/user/src", true, "contains");
        graph.addEdge("/home/user/src", "/home/user/src/site", true, "contains");
        graph.addEdge("/home/user/src/site", "/home/user/src/site/contacts", true, "contains");

        graph.addEdge("/home/user/binref", "/bin", true, "symlink");
        graph.addEdge("/var/www/site.lk", "/home/user/src/site", true, "symlink");


        assertTrue(graph.hasEdge("/", "/home", true, "contains"));
        assertFalse(graph.hasEdge("/home", "/", true, "contains"));
        assertFalse(graph.hasEdge("/", "/home", true, "symlink"));

        assertTrue(graph.hasEdge("/home/user/binref", "/bin", true, "symlink"));
        assertFalse(graph.hasEdge("/home/user/binref", "/etc", true, "symlink"));


        List<Edge<String, String>> path1 = graph.getPath("/", "/home/user/.m2", s -> true);
        logger.debug("path1: {}", path1);


        List<Edge<String, String>> path2 = graph.getPath("/", "/bin", s -> true);
        logger.debug("path2: {}", path2);

        List<Edge<String, String>> path3 = graph.getPath("/", "/home/user/src/site/contacts", s -> true);
        logger.debug("path3: {}", path3);

        List<Edge<String, String>> path4 = graph.getPath("/home/user/src/site", "/", s -> true);
        logger.debug("path4: {}", path4);


    }


    @Test
    void undirectedGraph() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("Chicago");
        graph.addVertex("Boston");
        graph.addVertex("New York");
        graph.addVertex("Detroit");
        graph.addVertex("Philadelphia");
        graph.addVertex("Berlin");


        graph.addEdge("Detroit", "Chicago", false, "237mi");
        graph.addEdge("Detroit", "Boston", false, "611mi");
        graph.addEdge("Detroit", "New York", false, "480mi");
        graph.addEdge("Detroit", "Philadelphia", false, "411mi");

        graph.addEdge("New York", "Chicago", false, "710mi");
        graph.addEdge("New York", "Philadelphia", false, "80mi");
        graph.addEdge("New York", "Boston", false, "190mi");

        graph.addEdge("Chicago", "Philadelphia", false, "663mi");


        graph.addEdge("Philadelphia", "Boston", false, "270mi");


        List<Edge<String, String>> path1 = graph.getPath("Chicago", "Boston", s -> true);
        assertFalse(path1.isEmpty());
        logger.debug("path1: {}", path1);

        List<Edge<String, String>> path2 = graph.getPath("Boston", "Chicago",
                s -> true);
        assertFalse(path2.isEmpty());
        logger.debug("path2: {}", path2);

        List<Edge<String, String>> path3 = graph.getPath("Chicago", "Berlin", s -> true);
        assertTrue(path3.isEmpty());
        logger.debug("path3: {}", path3);

    }

}
