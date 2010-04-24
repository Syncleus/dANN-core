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

import com.syncleus.dann.graph.*;
import java.util.*;

public class WeightedDirectedGrid extends AbstractBidirectedGraph<GridNode, WeightedDirectedEdge<GridNode>>
{
	final private GridNode[][] nodes;
	final private Set<GridNode> nodeSet = new HashSet<GridNode>();
	final private Set<WeightedDirectedEdge<GridNode>> edges = new HashSet<WeightedDirectedEdge<GridNode>>();
	final private Map<GridNode, Set<WeightedDirectedEdge<GridNode>>> outNeighborEdges = new HashMap<GridNode, Set<WeightedDirectedEdge<GridNode>>>();
	final private Map<GridNode, Set<GridNode>> outNeighborNodes = new HashMap<GridNode, Set<GridNode>>();
	final private Map<GridNode, Set<WeightedDirectedEdge<GridNode>>> inNeighborEdges = new HashMap<GridNode, Set<WeightedDirectedEdge<GridNode>>>();
	final private Map<GridNode, Set<GridNode>> inNeighborNodes = new HashMap<GridNode, Set<GridNode>>();

	public WeightedDirectedGrid(double[][] nodeWeights)
	{
		this.nodes = new GridNode[nodeWeights.length][nodeWeights[0].length];

		//construct nodes
		for(int y = 0; y < nodeWeights.length; y++)
			for(int x = 0; x < nodeWeights[0].length; x++)
			{
				nodes[y][x] = new GridNode(x,y, nodeWeights[y][x]);
				this.nodeSet.add(nodes[y][x]);
				this.inNeighborEdges.put(nodes[y][x], new HashSet<WeightedDirectedEdge<GridNode>>());
				this.inNeighborNodes.put(nodes[y][x], new HashSet<GridNode>());
				this.outNeighborEdges.put(nodes[y][x], new HashSet<WeightedDirectedEdge<GridNode>>());
				this.outNeighborNodes.put(nodes[y][x], new HashSet<GridNode>());
			}

		//connect nodes
		for(int y = 0; y < nodes.length; y++)
			for(int x = 0; x < nodes[0].length; x++)
			{
				//connect to the right
				if( x < nodes[0].length - 1)
				{
					SimpleWeightedDirectedEdge<GridNode> newEdge = new SimpleWeightedDirectedEdge<GridNode>(nodes[y][x], nodes[y][x+1], nodes[y][x+1].getWeight());
					this.edges.add(newEdge);
					this.outNeighborEdges.get(nodes[y][x]).add(newEdge);
					this.inNeighborEdges.get(nodes[y][x+1]).add(newEdge);
					this.outNeighborNodes.get(nodes[y][x]).add(nodes[y][x+1]);
					this.inNeighborNodes.get(nodes[y][x+1]).add(nodes[y][x]);

					newEdge = new SimpleWeightedDirectedEdge<GridNode>(nodes[y][x+1], nodes[y][x], nodes[y][x].getWeight());
					this.edges.add(newEdge);
					this.inNeighborEdges.get(nodes[y][x]).add(newEdge);
					this.outNeighborEdges.get(nodes[y][x+1]).add(newEdge);
					this.inNeighborNodes.get(nodes[y][x]).add(nodes[y][x+1]);
					this.outNeighborNodes.get(nodes[y][x+1]).add(nodes[y][x]);
				}
				//connect to the bottom
				if( y < nodes.length - 1)
				{
					SimpleWeightedDirectedEdge<GridNode> newEdge = new SimpleWeightedDirectedEdge<GridNode>(nodes[y][x], nodes[y+1][x], nodes[y+1][x].getWeight());
					this.edges.add(newEdge);
					this.outNeighborEdges.get(nodes[y][x]).add(newEdge);
					this.inNeighborEdges.get(nodes[y+1][x]).add(newEdge);
					this.outNeighborNodes.get(nodes[y][x]).add(nodes[y+1][x]);
					this.inNeighborNodes.get(nodes[y+1][x]).add(nodes[y][x]);

					newEdge = new SimpleWeightedDirectedEdge<GridNode>(nodes[y+1][x], nodes[y][x], nodes[y][x].getWeight());
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
	public Set<WeightedDirectedEdge<GridNode>> getEdges()
	{
		return Collections.unmodifiableSet(this.edges);
	}

	public Set<WeightedDirectedEdge<GridNode>> getAdjacentEdges(GridNode node)
	{
		Set<WeightedDirectedEdge<GridNode>> newEdges = new HashSet<WeightedDirectedEdge<GridNode>>(this.inNeighborEdges.get(node));
		newEdges.addAll(this.outNeighborEdges.get(node));
		return Collections.unmodifiableSet(newEdges);
	}

	public Set<WeightedDirectedEdge<GridNode>> getTraversableEdges(GridNode node)
	{
		Set<WeightedDirectedEdge<GridNode>> newEdges = new HashSet<WeightedDirectedEdge<GridNode>>(this.outNeighborEdges.get(node));
		return Collections.unmodifiableSet(newEdges);
	}

	public Set<WeightedDirectedEdge<GridNode>> getOutEdges(GridNode node)
	{
		return this.getTraversableEdges(node);
	}

	public Set<WeightedDirectedEdge<GridNode>> getInEdges(GridNode node)
	{
		Set<WeightedDirectedEdge<GridNode>> newEdges = new HashSet<WeightedDirectedEdge<GridNode>>(this.inNeighborEdges.get(node));
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
		return this.getAdjacentNodes(leftNode).contains(rightNode);
	}

	public List<GridNode> getAdjacentNodes(GridNode node)
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
