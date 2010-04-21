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

public class Grid extends AbstractBidirectedGraph<GridNode, BidirectedEdge<GridNode>, BidirectedWalk<GridNode, BidirectedEdge<GridNode>>>
{
	final private GridNode[][] nodes;
	final private Set<GridNode> nodeSet = new HashSet<GridNode>();
	final private Set<BidirectedEdge<GridNode>> edges = new HashSet<BidirectedEdge<GridNode>>();
	final private Map<GridNode, Set<BidirectedEdge<GridNode>>> neighborEdges = new HashMap<GridNode, Set<BidirectedEdge<GridNode>>>();
	final private Map<GridNode, Set<GridNode>> neighborNodes = new HashMap<GridNode, Set<GridNode>>();

	public Grid(double[][] nodeWeights)
	{
		this.nodes = new GridNode[nodeWeights.length][nodeWeights[0].length];

		//construct nodes
		for(int y = 0; y < nodeWeights.length; y++)
			for(int x = 0; x < nodeWeights[0].length; x++)
			{
				nodes[y][x] = new GridNode(x,y, nodeWeights[y][x]);
				this.nodeSet.add(nodes[y][x]);
				this.neighborEdges.put(nodes[y][x], new HashSet<BidirectedEdge<GridNode>>());
				this.neighborNodes.put(nodes[y][x], new HashSet<GridNode>());
			}

		//connect nodes
		for(int y = 0; y < nodes.length; y++)
			for(int x = 0; x < nodes[0].length; x++)
			{
				//connect to the right
				if( x < nodes[0].length - 1)
				{
					SimpleUndirectedEdge<GridNode> newEdge = new SimpleUndirectedEdge<GridNode>(nodes[y][x], nodes[y][x+1]);
					this.edges.add(newEdge);
					this.neighborEdges.get(nodes[y][x]).add(newEdge);
					this.neighborEdges.get(nodes[y][x+1]).add(newEdge);
					this.neighborNodes.get(nodes[y][x]).add(nodes[y][x+1]);
					this.neighborNodes.get(nodes[y][x+1]).add(nodes[y][x]);
				}
				//connect to the bottom
				if( y < nodes.length - 1)
				{
					SimpleUndirectedEdge<GridNode> newEdge = new SimpleUndirectedEdge<GridNode>(nodes[y][x], nodes[y+1][x]);
					this.edges.add(newEdge);
					this.neighborEdges.get(nodes[y][x]).add(newEdge);
					this.neighborEdges.get(nodes[y+1][x]).add(newEdge);
					this.neighborNodes.get(nodes[y][x]).add(nodes[y+1][x]);
					this.neighborNodes.get(nodes[y+1][x]).add(nodes[y][x]);
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
	public Set<BidirectedEdge<GridNode>> getEdges()
	{
		return Collections.unmodifiableSet(this.edges);
	}

	public Set<BidirectedEdge<GridNode>> getEdges(GridNode node)
	{
		return Collections.unmodifiableSet(this.neighborEdges.get(node));
	}

	public Set<BidirectedEdge<GridNode>> getTraversableEdges(GridNode node)
	{
		return this.getEdges(node);
	}

	public Set<BidirectedEdge<GridNode>> getOutEdges(GridNode node)
	{
		return this.getEdges(node);
	}

	public Set<BidirectedEdge<GridNode>> getInEdges(GridNode node)
	{
		return this.getEdges(node);
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
		return this.neighborNodes.get(leftNode).contains(rightNode);
	}

	public List<GridNode> getNeighbors(GridNode node)
	{
		return Collections.unmodifiableList(new ArrayList<GridNode>(this.neighborNodes.get(node)));
	}

	public List<GridNode> getTraversableNeighbors(GridNode node)
	{
		return this.getNeighbors(node);
	}
}
