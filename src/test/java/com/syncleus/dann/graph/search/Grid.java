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
package com.syncleus.dann.graph.search;

import com.syncleus.dann.graph.*;

import java.util.*;

public class Grid extends AbstractBidirectedAdjacencyGraph<GridNode, BidirectedEdge<GridNode>> {
    private static final long serialVersionUID = -5202664944476006671L;
    private final GridNode[][] nodes;
    private final Set<GridNode> nodeSet = new HashSet<GridNode>();
    private final Set<BidirectedEdge<GridNode>> edges = new HashSet<BidirectedEdge<GridNode>>();
    private final Map<GridNode, Set<BidirectedEdge<GridNode>>> neighborEdges = new HashMap<GridNode, Set<BidirectedEdge<GridNode>>>();
    private final Map<GridNode, Set<GridNode>> neighborNodes = new HashMap<GridNode, Set<GridNode>>();

    public Grid(final double[][] nodeWeights) {
        this.nodes = new GridNode[nodeWeights.length][nodeWeights[0].length];
        //construct nodes
        for (int y = 0; y < nodeWeights.length; y++)
            for (int x = 0; x < nodeWeights[0].length; x++) {
                this.nodes[y][x] = new GridNode(x, y, nodeWeights[y][x]);
                this.nodeSet.add(this.nodes[y][x]);
                this.neighborEdges.put(this.nodes[y][x], new HashSet<BidirectedEdge<GridNode>>());
                this.neighborNodes.put(this.nodes[y][x], new HashSet<GridNode>());
            }
        //connect nodes
        for (int y = 0; y < nodes.length; y++)
            for (int x = 0; x < this.nodes[0].length; x++) {
                //connect to the right
                if (x < this.nodes[0].length - 1) {
                    final ImmutableUndirectedEdge<GridNode> newEdge = new ImmutableUndirectedEdge<GridNode>(this.nodes[y][x], this.nodes[y][x + 1]);
                    this.edges.add(newEdge);
                    this.neighborEdges.get(this.nodes[y][x]).add(newEdge);
                    this.neighborEdges.get(this.nodes[y][x + 1]).add(newEdge);
                    this.neighborNodes.get(this.nodes[y][x]).add(this.nodes[y][x + 1]);
                    this.neighborNodes.get(this.nodes[y][x + 1]).add(this.nodes[y][x]);
                }
                //connect to the bottom
                if (y < nodes.length - 1) {
                    final ImmutableUndirectedEdge<GridNode> newEdge = new ImmutableUndirectedEdge<GridNode>(this.nodes[y][x], this.nodes[y + 1][x]);
                    this.edges.add(newEdge);
                    this.neighborEdges.get(this.nodes[y][x]).add(newEdge);
                    this.neighborEdges.get(this.nodes[y + 1][x]).add(newEdge);
                    this.neighborNodes.get(this.nodes[y][x]).add(this.nodes[y + 1][x]);
                    this.neighborNodes.get(this.nodes[y + 1][x]).add(this.nodes[y][x]);
                }
            }
    }

    public GridNode getNode(final int x, final int y) {
        if ((x >= this.nodes[0].length) || (y >= nodes.length))
            throw new IllegalArgumentException("coordinates are out of bounds");
        return this.nodes[y][x];
    }

    public Set<GridNode> getNodes() {
        return Collections.unmodifiableSet(this.nodeSet);
    }

    @Override
    public Set<BidirectedEdge<GridNode>> getEdges() {
        return Collections.unmodifiableSet(this.edges);
    }

    public Set<BidirectedEdge<GridNode>> getAdjacentEdges(final GridNode node) {
        return Collections.unmodifiableSet(this.neighborEdges.get(node));
    }

    public Set<BidirectedEdge<GridNode>> getTraversableEdges(final GridNode node) {
        return this.getAdjacentEdges(node);
    }

    public Set<BidirectedEdge<GridNode>> getOutEdges(final GridNode node) {
        return this.getAdjacentEdges(node);
    }

    public Set<BidirectedEdge<GridNode>> getInEdges(final GridNode node) {
        return this.getAdjacentEdges(node);
    }

    public int getIndegree(final GridNode node) {
        return this.getInEdges(node).size();
    }

    public int getOutdegree(final GridNode node) {
        return this.getOutEdges(node).size();
    }

    public boolean isStronglyConnected(final GridNode leftNode, final GridNode rightNode) {
        return this.neighborNodes.get(leftNode).contains(rightNode);
    }

    public List<GridNode> getAdjacentNodes(final GridNode node) {
        return Collections.unmodifiableList(new ArrayList<GridNode>(this.neighborNodes.get(node)));
    }

    public List<GridNode> getTraversableNodes(final GridNode node) {
        return this.getAdjacentNodes(node);
    }
}
