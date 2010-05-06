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
package com.syncleus.dann.graph;

import java.util.*;

public class SimpleMutableTreeGraph<N, E extends BidirectedEdge<N>>  extends AbstractTreeGraph<N, E> implements MutableTreeGraph<N,E>
{
	private final Set<E> edges;

	private final Map<N, List<N>> adjacentNodes = new HashMap<N, List<N>>();
	private final Map<N, Set<E>> adjacentEdges = new HashMap<N, Set<E>>();

	public SimpleMutableTreeGraph(Set<N> nodes, Set<E> edges)
	{
		this.edges = new HashSet<E>(edges);

		for(N node : nodes)
		{
			this.adjacentNodes.put(node, new ArrayList<N>());
			this.adjacentEdges.put(node, new HashSet<E>());
		}

		for(E edge : edges)
		{
			final List<N> edgeNodes = edge.getNodes();
			for(int startNodeIndex = 0; startNodeIndex < edgeNodes.size(); startNodeIndex++)
			{
				N edgeNode = edgeNodes.get(startNodeIndex);

				if(!nodes.contains(edgeNode))
					throw new IllegalArgumentException("A node that is an end point in one of the edges was not in the nodes list");

				this.adjacentEdges.get(edgeNode).add(edge);

				for(int endNodeIndex = 0; endNodeIndex < edgeNodes.size(); endNodeIndex++)
				{
					if(startNodeIndex != endNodeIndex)
						this.adjacentNodes.get(edgeNode).add(edgeNodes.get(endNodeIndex));
				}
			}
		}
	}

	public boolean add(E newEdge)
	{
		if(newEdge == null)
			throw new IllegalArgumentException("newEdge can not be null");
		if(!this.getNodes().containsAll(newEdge.getNodes()))
			throw new IllegalArgumentException("newEdge has a node as an end point that is not part of the graph");

		if(this.edges.add(newEdge))
		{
			for(N currentNode : newEdge.getNodes())
			{
				this.adjacentEdges.get(currentNode).add(newEdge);

				List<N> newAdjacentNodes = new ArrayList<N>(newEdge.getNodes());
				newAdjacentNodes.remove(currentNode);
				for(N newAdjacentNode : newAdjacentNodes)
					this.adjacentNodes.get(currentNode).add(newAdjacentNode);
			}
			return true;
		}

		return false;
	}

	public boolean add(N newNode)
	{
		if(newNode == null)
			throw new IllegalArgumentException("newNode can not be null");

		if(this.getNodes().contains(newNode))
			return false;

		this.adjacentEdges.put(newNode, new HashSet<E>());
		this.adjacentNodes.put(newNode, new ArrayList<N>());
		return true;
	}

	public boolean remove(E edgeToRemove)
	{
		if(edgeToRemove == null)
			throw new IllegalArgumentException("removeSynapse can not be null");

		if(!this.edges.remove(edgeToRemove))
			return false;

		for(N removeNode : edgeToRemove.getNodes())
		{
			this.adjacentEdges.get(removeNode).remove(edgeToRemove);

			List<N> removeAdjacentNodes = new ArrayList<N>(edgeToRemove.getNodes());
			removeAdjacentNodes.remove(removeNode);
			for(N removeAdjacentNode : removeAdjacentNodes)
				this.adjacentNodes.get(removeNode).remove(removeAdjacentNode);
		}
		return true;
	}

	public boolean remove(N nodeToRemove)
	{
		if(nodeToRemove == null)
			throw new IllegalArgumentException("node can not be null");

		if(!this.getNodes().contains(nodeToRemove))
			return false;

		Set<E> removeEdges = this.adjacentEdges.get(nodeToRemove);

		//remove all the edges
		for(E removeEdge : removeEdges)
			this.remove(removeEdge);

		//modify edges by removing the node to remove
		Set<E> newEdges = new HashSet<E>();
		for(E removeEdge : removeEdges)
		{
			E newEdge = (E) removeEdge.disconnect(nodeToRemove);
			while( (newEdge.getNodes().contains(nodeToRemove)) && (newEdge != null) )
				newEdge = (E) removeEdge.disconnect(nodeToRemove);
			if(newEdge != null)
				newEdges.add(newEdge);
		}

		//add the modified edges
		for(E newEdge : newEdges)
			this.add(newEdge);

		//remove the node itself
		this.adjacentEdges.remove(nodeToRemove);
		this.adjacentNodes.remove(nodeToRemove);

		return true;
	}

	public Set<N> getNodes()
	{
		return Collections.unmodifiableSet(this.adjacentNodes.keySet());
	}

	public Set<E> getEdges()
	{
		return Collections.unmodifiableSet(this.edges);
	}

	public Set<E> getAdjacentEdges(N node)
	{
		if(node == null)
			throw new IllegalArgumentException("node can not be null", new NullPointerException());
		if(!this.getNodes().contains(node))
			throw new IllegalArgumentException("node is not part of this graph");

		return Collections.unmodifiableSet(this.adjacentEdges.get(node));
	}

	public List<N> getAdjacentNodes(N node)
	{
		if(node == null)
			throw new IllegalArgumentException("node can not be null", new NullPointerException());
		if(!this.getNodes().contains(node))
			throw new IllegalArgumentException("node is not part of this graph");

		return Collections.unmodifiableList(this.adjacentNodes.get(node));
	}

	public Set<E> getInEdges(final N node)
	{
		return this.getAdjacentEdges(node);
	}

	public int getIndegree(final N node)
	{
		return this.getInEdges(node).size();
	}

	public int getOutdegree(final N node)
	{
		return this.getTraversableEdges(node).size();
	}
}
