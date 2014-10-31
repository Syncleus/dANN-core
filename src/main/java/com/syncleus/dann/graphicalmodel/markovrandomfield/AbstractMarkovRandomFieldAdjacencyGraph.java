/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graphicalmodel.markovrandomfield;

import com.syncleus.dann.graph.*;
import com.syncleus.dann.graphicalmodel.*;

import java.util.*;

public abstract class AbstractMarkovRandomFieldAdjacencyGraph<N extends GraphicalModelNode, E extends UndirectedEdge<N>> extends AbstractGraphicalModelAdjacencyGraph<N, E> {
    protected AbstractMarkovRandomFieldAdjacencyGraph() {
        super();
    }

    protected AbstractMarkovRandomFieldAdjacencyGraph(final Graph<N, E> copyGraph) {
        super(copyGraph.getNodes(), copyGraph.getEdges());
    }

    protected AbstractMarkovRandomFieldAdjacencyGraph(final Set<N> nodes, final Set<E> edges) {
        super(nodes, edges);
    }

    @Override
    public double jointProbability() {
        final Set<N> seenNodes = new HashSet<N>();
        double probabilityProduct = 1.0;
        for (final N node : this.getNodes()) {
            assert !seenNodes.contains(node);

            probabilityProduct *= node.stateProbability(seenNodes);

            seenNodes.add(node);
        }
        return probabilityProduct;
    }
}