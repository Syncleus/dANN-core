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

import com.syncleus.dann.graph.AbstractBidirectedGraph;
import com.syncleus.dann.graph.BidirectedWalk;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.SimpleDirectedEdge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DirectedGrid extends AbstractBidirectedGraph<GridNode, DirectedEdge<GridNode>, BidirectedWalk<GridNode, DirectedEdge<GridNode>>>
{
	final private GridNode[][] nodes;
	final private Set<GridNode> nodeSet = new HashSet<GridNode>();
	final private Set<DirectedEdge<GridNode>> edges = new HashSet<DirectedEdge<GridNode>>();
	final private Map<GridNode, Set<DirectedEdge<GridNode>>> outNeighborEdges = new HashMap<GridNode, Set<DirectedEdge<GridNode>>>();
	final private Map<GridNode, Set<GridNode>> outNeighborNodes = new HashMap<GridNode, Set<GridNode>>();
	final private Map<GridNode, Set<DirectedEdge<GridNode>>> inNeighborEdges = new HashMap<GridNode, Set<DirectedEdge<GridNode>>>();
	final private Map<GridNode, Set<GridNode>> inNeighborNodes = new HashMap<GridNode, Set<GridNode>>();

	public DirectedGrid(double[][] nodeWeights)
	{
		this.nodes = new GridNode[nodeWeights.length][nodeWeights[0].length];

		//construct nodes
		for(int y = 0; y < nodeWeights.length; y++)
			for(int x = 0; x < nodeWeights[0].length; x++)
			{
				nodes[y][x] = new GridNode(x,y, nodeWeights[y][x]);
				this.nodeSet.add(nodes[y][x]);
				this.inNeighborEdges.put(nodes[y][x], new HashSet<DirectedEdge<GridNode>>());
				this.inNeighborNodes.put(nodes[y][x], new HashSet<GridNode>());
				this.outNeighborEdges.put(nodes[y][x], new HashSet<DirectedEdge<GridNode>>());
				this.outNeighborNodes.put(nodes[y][x], new HashSet<GridNode>());
			}

		//connect nodes
		for(int y = 0; y < nodes.length; y++)
			for(int x = 0; x < nodes[0].length; x++)
			{
				//connect to the right
				if( x < nodes[0].length - 1)
				{
					SimpleDirectedEdge<GridNode> newEdge = new SimpleDirectedEdge<GridNode>(nodes[y][x], nodes[y][x+1]);
					this.edges.add(newEdge);
					this.outNeighborEdges.get(nodes[y][x]).add(newEdge);
					this.inNeighborEdges.get(nodes[y][x+1]).add(newEdge);
					this.outNeighborNodes.get(nodes[y][x]).add(nodes[y][x+1]);
					this.inNeighborNodes.get(nodes[y][x+1]).add(nodes[y][x]);

					newEdge = new SimpleDirectedEdge<GridNode>(nodes[y][x+1], nodes[y][x]);
					this.edges.add(newEdge);
					this.inNeighborEdges.get(nodes[y][x]).add(newEdge);
					this.outNeighborEdges.get(nodes[y][x+1]).add(newEdge);
					this.inNeighborNodes.get(nodes[y][x]).add(nodes[y][x+1]);
					this.outNeighborNodes.get(nodes[y][x+1]).add(nodes[y][x]);
				}
				//connect to the bottom
				if( y < nodes.length - 1)
				{
					SimpleDirectedEdge<GridNode> newEdge = new SimpleDirectedEdge<GridNode>(nodes[y][x], nodes[y+1][x]);
					this.edges.add(newEdge);
					this.outNeighborEdges.get(nodes[y][x]).add(newEdge);
					this.inNeighborEdges.get(nodes[y+1][x]).add(newEdge);
					this.outNeighborNodes.get(nodes[y][x]).add(nodes[y+1][x]);
					this.inNeighborNodes.get(nodes[y+1][x]).add(nodes[y][x]);

					newEdge = new SimpleDirectedEdge<GridNode>(nodes[y+1][x], nodes[y][x]);
					this.edges.add(newEdge);
					this.inNeighborEdges.get(nodes[y][x]).add(newEdge);
					this.outNeighborEdges.get(nodes[y+1][x]).add(newEdge);
					this.inNeighborNodes.get(nodes[y][x]).add(nodes[y+1][x]);
					this.outNeighborNodes.get(nodes[y+1][x]).add(nodes[y][x]);
				}
			}
	}

	public GridNode getNode(int x, int y)
	{
		if( (x >= nodes[0].length)||(y >= nodes.length) )
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

	public Set<DirectedEdge<GridNode>> getEdges(GridNode node)
	{
		Set<DirectedEdge<GridNode>> newEdges = new HashSet<DirectedEdge<GridNode>>(this.inNeighborEdges.get(node));
		newEdges.addAll(this.outNeighborEdges.get(node));
		return Collections.unmodifiableSet(newEdges);
	}

	public Set<DirectedEdge<GridNode>> getTraversableEdges(GridNode node)
	{
		Set<DirectedEdge<GridNode>> newEdges = new HashSet<DirectedEdge<GridNode>>(this.outNeighborEdges.get(node));
		return Collections.unmodifiableSet(newEdges);
	}

	public Set<DirectedEdge<GridNode>> getOutEdges(GridNode node)
	{
		return this.getTraversableEdges(node);
	}

	public Set<DirectedEdge<GridNode>> getInEdges(GridNode node)
	{
		Set<DirectedEdge<GridNode>> newEdges = new HashSet<DirectedEdge<GridNode>>(this.inNeighborEdges.get(node));
		return Collections.unmodifiableSet(newEdges);
	}

	public int getIndegree(GridNode node)
	{
		return this.getInEdges(node).size();
	}

	public int getOutdegree(GridNode node)
	{
		return this.getOutEdges(node).size();
	}

	public boolean isConnected(GridNode leftNode, GridNode rightNode)
	{
		return this.getNeighbors(leftNode).contains(rightNode);
	}

	public List<GridNode> getNeighbors(GridNode node)
	{
		ArrayList<GridNode> newNeighbors = new ArrayList<GridNode>(this.inNeighborNodes.get(node));
		newNeighbors.addAll(this.outNeighborNodes.get(node));
		return Collections.unmodifiableList(newNeighbors);
	}

	public List<GridNode> getTraversableNeighbors(GridNode node)
	{
		ArrayList<GridNode> newNeighbors = new ArrayList<GridNode>(this.outNeighborNodes.get(node));
		return Collections.unmodifiableList(newNeighbors);
	}
}
