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
package com.syncleus.tests.dann.graph.search;

import java.util.*;
import com.syncleus.dann.graph.*;

public class DirectedGrid extends AbstractBidirectedAdjacencyGraph<GridNode, DirectedEdge<GridNode>>
{
	private final GridNode[][] nodes;
	private final Set<GridNode> nodeSet = new HashSet<GridNode>();
	private final Set<DirectedEdge<GridNode>> edges = new HashSet<DirectedEdge<GridNode>>();
	private final Map<GridNode, Set<DirectedEdge<GridNode>>> outNeighborEdges = new HashMap<GridNode, Set<DirectedEdge<GridNode>>>();
	private final Map<GridNode, Set<GridNode>> outNeighborNodes = new HashMap<GridNode, Set<GridNode>>();
	private final Map<GridNode, Set<DirectedEdge<GridNode>>> inNeighborEdges = new HashMap<GridNode, Set<DirectedEdge<GridNode>>>();
	private final Map<GridNode, Set<GridNode>> inNeighborNodes = new HashMap<GridNode, Set<GridNode>>();
	private static final long serialVersionUID = -3051997268757591022L;

	public DirectedGrid(final double[][] nodeWeights)
	{
		this.nodes = new GridNode[nodeWeights.length][nodeWeights[0].length];
		//construct nodes
		for(int y = 0; y < nodeWeights.length; y++)
			for(int x = 0; x < nodeWeights[0].length; x++)
			{
				this.nodes[y][x] = new GridNode(x, y, nodeWeights[y][x]);
				this.nodeSet.add(this.nodes[y][x]);
				this.inNeighborEdges.put(this.nodes[y][x], new HashSet<DirectedEdge<GridNode>>());
				this.inNeighborNodes.put(this.nodes[y][x], new HashSet<GridNode>());
				this.outNeighborEdges.put(this.nodes[y][x], new HashSet<DirectedEdge<GridNode>>());
				this.outNeighborNodes.put(this.nodes[y][x], new HashSet<GridNode>());
			}
		//connect nodes
		for(int y = 0; y < nodes.length; y++)
			for(int x = 0; x < this.nodes[0].length; x++)
			{
				//connect to the right
				if( x < this.nodes[0].length - 1 )
				{
					ImmutableDirectedEdge<GridNode> newEdge = new ImmutableDirectedEdge<GridNode>(this.nodes[y][x], this.nodes[y][x + 1]);
					this.edges.add(newEdge);
					this.outNeighborEdges.get(this.nodes[y][x]).add(newEdge);
					this.inNeighborEdges.get(this.nodes[y][x + 1]).add(newEdge);
					this.outNeighborNodes.get(this.nodes[y][x]).add(this.nodes[y][x + 1]);
					this.inNeighborNodes.get(this.nodes[y][x + 1]).add(this.nodes[y][x]);
					newEdge = new ImmutableDirectedEdge<GridNode>(this.nodes[y][x + 1], this.nodes[y][x]);
					this.edges.add(newEdge);
					this.inNeighborEdges.get(this.nodes[y][x]).add(newEdge);
					this.outNeighborEdges.get(this.nodes[y][x + 1]).add(newEdge);
					this.inNeighborNodes.get(this.nodes[y][x]).add(this.nodes[y][x + 1]);
					this.outNeighborNodes.get(this.nodes[y][x + 1]).add(this.nodes[y][x]);
				}
				//connect to the bottom
				if( y < nodes.length - 1 )
				{
					ImmutableDirectedEdge<GridNode> newEdge = new ImmutableDirectedEdge<GridNode>(this.nodes[y][x], this.nodes[y + 1][x]);
					this.edges.add(newEdge);
					this.outNeighborEdges.get(this.nodes[y][x]).add(newEdge);
					this.inNeighborEdges.get(this.nodes[y + 1][x]).add(newEdge);
					this.outNeighborNodes.get(this.nodes[y][x]).add(this.nodes[y + 1][x]);
					this.inNeighborNodes.get(this.nodes[y + 1][x]).add(this.nodes[y][x]);
					newEdge = new ImmutableDirectedEdge<GridNode>(this.nodes[y + 1][x], this.nodes[y][x]);
					this.edges.add(newEdge);
					this.inNeighborEdges.get(this.nodes[y][x]).add(newEdge);
					this.outNeighborEdges.get(this.nodes[y + 1][x]).add(newEdge);
					this.inNeighborNodes.get(this.nodes[y][x]).add(this.nodes[y + 1][x]);
					this.outNeighborNodes.get(this.nodes[y + 1][x]).add(this.nodes[y][x]);
				}
			}
	}

	public GridNode getNode(final int x, final int y)
	{
		if( (x >= this.nodes[0].length) || (y >= nodes.length) )
			throw new IllegalArgumentException("coordinates are out of bounds");
		return this.nodes[y][x];
	}

	public Set<GridNode> getNodes()
	{
		return Collections.unmodifiableSet(this.nodeSet);
	}

	@Override
	public Set<DirectedEdge<GridNode>> getEdges()
	{
		return Collections.unmodifiableSet(this.edges);
	}

	public Set<DirectedEdge<GridNode>> getAdjacentEdges(final GridNode node)
	{
		final Set<DirectedEdge<GridNode>> newEdges = new HashSet<DirectedEdge<GridNode>>(this.inNeighborEdges.get(node));
		newEdges.addAll(this.outNeighborEdges.get(node));
		return Collections.unmodifiableSet(newEdges);
	}

	public Set<DirectedEdge<GridNode>> getTraversableEdges(final GridNode node)
	{
		final Set<DirectedEdge<GridNode>> newEdges = new HashSet<DirectedEdge<GridNode>>(this.outNeighborEdges.get(node));
		return Collections.unmodifiableSet(newEdges);
	}

	public Set<DirectedEdge<GridNode>> getOutEdges(final GridNode node)
	{
		return this.getTraversableEdges(node);
	}

	public Set<DirectedEdge<GridNode>> getInEdges(final GridNode node)
	{
		final Set<DirectedEdge<GridNode>> newEdges = new HashSet<DirectedEdge<GridNode>>(this.inNeighborEdges.get(node));
		return Collections.unmodifiableSet(newEdges);
	}

	public int getIndegree(final GridNode node)
	{
		return this.getInEdges(node).size();
	}

	public int getOutdegree(final GridNode node)
	{
		return this.getOutEdges(node).size();
	}

	public boolean isStronglyConnected(final GridNode leftNode, final GridNode rightNode)
	{
		return this.getAdjacentNodes(leftNode).contains(rightNode);
	}

	public List<GridNode> getAdjacentNodes(final GridNode node)
	{
		final ArrayList<GridNode> newNeighbors = new ArrayList<GridNode>(this.inNeighborNodes.get(node));
		newNeighbors.addAll(this.outNeighborNodes.get(node));
		return Collections.unmodifiableList(newNeighbors);
	}

	public List<GridNode> getTraversableNodes(final GridNode node)
	{
		final ArrayList<GridNode> newNeighbors = new ArrayList<GridNode>(this.outNeighborNodes.get(node));
		return Collections.unmodifiableList(newNeighbors);
	}
}
