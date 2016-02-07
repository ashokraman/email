package com.galaxyadvisors.emaildata

import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory

/**
 * Created by Ashok on 31-12-2015.
 */
import com.tinkerpop.gremlin.groovy.Gremlin

class EmailActivity {
    static {
        Gremlin.load()
    }
    public static List exampleMethod() {
        Graph g = TinkerGraphFactory.createTinkerGraph()
        def results = []
        g.v(1).out('knows').fill(results)
        return results
    }
    public static Map<Vertex, Integer> eigenvectorRank(Graph g) {
        Map<Vertex,Integer> m = [:]; int c = 0
        g.V.as('x').out.groupCount(m).loop('x') {c++ < 1000}.iterate()
        return m
    }
}
