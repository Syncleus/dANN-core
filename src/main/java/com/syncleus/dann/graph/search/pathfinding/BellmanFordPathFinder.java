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
package com.syncleus.dann.graph.search.pathfinding;

import com.syncleus.dann.graph.*;

import java.util.*;

public class BellmanFordPathFinder<N, E extends DirectedEdge<N>> implements PathFinder<N, E> {
    private final Graph<N, E> graph;
    private Map<N, PathedStep> pathedSteps;

    public BellmanFordPathFinder(final Graph<N, E> graph) {
        if (graph == null)
            throw new IllegalArgumentException("graph can not be null");
        this.graph = graph;
    }

    @Override
    public List<E> getBestPath(final N begin, final N end) {
        return this.getBestPath(begin, end, true);
    }

    public List<E> getBestPath(final N begin, final N end, final boolean refresh) {
        if ((refresh) || (this.pathedSteps == null))
            this.calculateSteps(begin);
        //construct a walk from the end node
        final PathedStep endPathedStep = this.pathedSteps.get(end);
        final PathedStep beginPathedStep = this.pathedSteps.get(begin);
        assert ((endPathedStep != null) && (beginPathedStep != null));
        return this.pathedStepToWalk(endPathedStep);
    }

    public void calculateSteps(final N begin) {
        final Set<? extends N> nodes = this.graph.getNodes();
        final Set<? extends E> edges = this.graph.getEdges();
        this.pathedSteps = new HashMap<N, PathedStep>(nodes.size());
        //relax edges
        for (int lcv = 0; lcv < (nodes.size() - 1); lcv++) {
            for (final E edge : edges) {
                if (edge.getDestinationNode() == begin)
                    continue;
                PathedStep sourcePathedStep = this.pathedSteps.get(edge.getSourceNode());
                if (sourcePathedStep == null) {
                    sourcePathedStep = new PathedStep(edge.getSourceNode(), (edge.getSourceNode().equals(begin) ? 0.0 : Double.POSITIVE_INFINITY));
                    this.pathedSteps.put(edge.getSourceNode(), sourcePathedStep);
                }
                PathedStep destinationPathedStep = this.pathedSteps.get(edge.getDestinationNode());
                if (destinationPathedStep == null) {
                    destinationPathedStep = new PathedStep(edge.getDestinationNode(), Double.POSITIVE_INFINITY);
                    this.pathedSteps.put(edge.getDestinationNode(), destinationPathedStep);
                }
                destinationPathedStep.updateParent(sourcePathedStep, edge);
            }
        }
        //check for negative cycles
        for (final E edge : edges) {
            if (edge.getDestinationNode() == begin)
                continue;
            final PathedStep sourcePathedStep = this.pathedSteps.get(edge.getSourceNode());
            final PathedStep destinationPathedStep = this.pathedSteps.get(edge.getDestinationNode());
            assert ((sourcePathedStep != null) && (destinationPathedStep != null));
            if (destinationPathedStep.updateParent(sourcePathedStep, edge))
                throw new NegativeWeightCycleException("negative-weight cycle found in graph");
        }
    }

    @Override
    public boolean isReachable(final N begin, final N end) {
        return (this.getBestPath(begin, end) != null);
    }

    @Override
    public boolean isConnected(final N begin, final N end) {
        return (this.getBestPath(begin, end) != null);
    }

    private List<E> pathedStepToWalk(final PathedStep endPathedStep) {
        final List<E> edges = new ArrayList<E>();
        PathedStep currentStep = endPathedStep;
        while (currentStep != null) {
            if (currentStep.getParentEdge() != null)
                edges.add(0, currentStep.getParentEdge());
            currentStep = currentStep.getParent();
        }
        return edges;
    }

    private final class PathedStep implements Comparable<PathedStep> {
        private final N node;
        private PathedStep parent;
        private E parentEdge;
        private double cachedPathWeight;

        public PathedStep(final N node, final double initialWeight) {
            if (node == null)
                throw new IllegalArgumentException("node can not be null");

            this.node = node;
            this.cachedPathWeight = initialWeight;
        }

        private boolean updateParent(final PathedStep newParent, final E newParentEdge) {
            double newWeight = (newParentEdge instanceof Weighted ? ((Weighted) newParentEdge).getWeight() : 1.0);
            if (this.node instanceof Weighted)
                newWeight += ((Weighted) this.node).getWeight();
            if ((this.parent == null) || (newParent.cachedPathWeight + newWeight < this.cachedPathWeight)) {
                this.parent = newParent;
                this.parentEdge = newParentEdge;
                this.cachedPathWeight = newParent.cachedPathWeight + newWeight;
                return true;
            }
            else
                return false;
        }

        public N getNode() {
            return this.node;
        }

        public PathedStep getParent() {
            return this.parent;
        }

        public double getCachedPathWeight() {
            return this.cachedPathWeight;
        }

        @Override
        public boolean equals(final Object compareToObj) {
            if (compareToObj == null)
                return false;

            if (!(compareToObj.getClass().isInstance(this)))
                return false;

            final PathedStep compareTo = (PathedStep) compareToObj;
            return ((this.node.equals(compareTo)) || (this.node.equals(compareTo.node)));
        }

        @Override
        public int hashCode() {
            return this.node.hashCode();
        }

        @Override
        public int compareTo(final PathedStep compareWith) {
            //the natural ordering is inverse cause the smallest path weight is
            //the best weight.
            if (this.cachedPathWeight < compareWith.cachedPathWeight)
                return -1;
            else if (this.cachedPathWeight > compareWith.cachedPathWeight)
                return 1;
            else
                return 0;
        }

        public E getParentEdge() {
            return this.parentEdge;
        }

        @Override
        public String toString() {
            return this.node.toString();
        }
    }
}
