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

import com.syncleus.dann.graph.AbstractGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractBidirectedGraph<G extends BidirectedGraph<? extends G, ? extends N, ? extends E, ? extends W>, N extends BidirectedNode<? extends E>, E extends BidirectedEdge<? extends N>, W extends BidirectedWalk<? extends N, ? extends E>> extends AbstractGraph<G, N, E, W> implements BidirectedGraph<G, N, E, W>
{
	public List<E> getEdges()
	{
		List<E> allEdges = new ArrayList<E>();
		Set<N> allNodes = this.getNodes();
		Set<N> traversedNodes = new HashSet<N>();
		for(N node : allNodes)
		{
			List<? extends E> currentEdges = node.getEdges();

			for(E edge : currentEdges)
			{
				NodePair<? extends N> currentNodePair = edge.getNodePair();
				if((!traversedNodes.contains(currentNodePair.getLeftNode()))&&(!traversedNodes.contains(currentNodePair.getRightNode())))
					allEdges.add(edge);
			}

			traversedNodes.add(node);
		}

		return Collections.unmodifiableList(allEdges);
	}

	public boolean isStronglyConnected()
	{
		return false;
	}

	public Set<G> getStrongComponents()
	{
		return null;
	}

	public boolean isPolytree()
	{
		return false;
	}
}
