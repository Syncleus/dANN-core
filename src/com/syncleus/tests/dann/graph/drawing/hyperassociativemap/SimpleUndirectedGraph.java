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
package com.syncleus.tests.dann.graph.drawing.hyperassociativemap;

import com.syncleus.dann.graph.*;
import java.util.*;

public class SimpleUndirectedGraph extends AbstractBidirectedGraph<SimpleNode, BidirectedEdge<SimpleNode>>
{
	final private SimpleNode[][] nodes;
	final private Set<SimpleNode> nodeSet = new HashSet<SimpleNode>();
	final private Set<BidirectedEdge<SimpleNode>> edges = new HashSet<BidirectedEdge<SimpleNode>>();
	final private Map<SimpleNode, Set<BidirectedEdge<SimpleNode>>> neighborEdges = new HashMap<SimpleNode, Set<BidirectedEdge<SimpleNode>>>();
	final private Map<SimpleNode, List<SimpleNode>> neighborNodes = new HashMap<SimpleNode, List<SimpleNode>>();

	public SimpleUndirectedGraph(int layers, int nodesPerLayer)
	{
		this.nodes = new SimpleNode[layers][nodesPerLayer];

		//construct nodes
		for(int layerIndex = 0; layerIndex < layers; layerIndex++)
		{
			for(int nodeIndex = 0; nodeIndex < nodesPerLayer; nodeIndex++)
			{
				nodes[layerIndex][nodeIndex] = new SimpleNode(layerIndex);
				this.nodeSet.add(nodes[layerIndex][nodeIndex]);
				this.neighborEdges.put(nodes[layerIndex][nodeIndex], new HashSet<BidirectedEdge<SimpleNode>>());
				this.neighborNodes.put(nodes[layerIndex][nodeIndex], new ArrayList<SimpleNode>());
			}
		}

		//connect nodes
		for(int layerIndex = 0; layerIndex < (layers-1); layerIndex++)
			for(int nodeIndex = 0; nodeIndex < nodesPerLayer; nodeIndex++)
			{
				for(int nodeIndex2 = 0; nodeIndex2 < nodesPerLayer; nodeIndex2++)
				{
					SimpleUndirectedEdge<SimpleNode> newEdge = new SimpleUndirectedEdge<SimpleNode>(nodes[layerIndex][nodeIndex], nodes[layerIndex+1][nodeIndex2]);
					this.edges.add(newEdge);
					this.neighborEdges.get(nodes[layerIndex][nodeIndex]).add(newEdge);
					this.neighborNodes.get(nodes[layerIndex][nodeIndex]).add(nodes[layerIndex+1][nodeIndex2]);
					this.neighborEdges.get(nodes[layerIndex+1][nodeIndex2]).add(newEdge);
					this.neighborNodes.get(nodes[layerIndex+1][nodeIndex2]).add(nodes[layerIndex][nodeIndex]);
				}
			}
	}

	public SimpleNode[][] getNodeInLayers()
	{
		return this.nodes;
	}

	public SimpleNode getNode(int layer, int index)
	{
		if( (index >= nodes[0].length)||(layer >= nodes.length) )
			throw new IllegalArgumentException("coordinates are out of bounds");
		return this.nodes[layer][index];
	}

	public Set<SimpleNode> getNodes()
	{
		return Collections.unmodifiableSet(this.nodeSet);
	}

	@Override
	public Set<BidirectedEdge<SimpleNode>> getEdges()
	{
		return Collections.unmodifiableSet(this.edges);
	}

	public Set<BidirectedEdge<SimpleNode>> getAdjacentEdges(SimpleNode node)
	{
		return Collections.unmodifiableSet(this.neighborEdges.get(node));
	}

	public Set<BidirectedEdge<SimpleNode>> getInEdges(SimpleNode node)
	{
		return this.getAdjacentEdges(node);
	}

	public int getIndegree(SimpleNode node)
	{
		return this.getInEdges(node).size();
	}

	public int getOutdegree(SimpleNode node)
	{
		return this.getTraversableEdges(node).size();
	}

	public List<SimpleNode> getAdjacentNodes(SimpleNode node)
	{
		return Collections.unmodifiableList(this.neighborNodes.get(node));
	}
}
