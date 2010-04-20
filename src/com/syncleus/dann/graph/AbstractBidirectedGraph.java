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

import java.util.List;
import java.util.Set;

public abstract class AbstractBidirectedGraph<N, E extends BidirectedEdge<? extends N>, W extends BidirectedWalk<? extends N, ? extends E>> extends AbstractGraph<N,E,W> implements BidirectedGraph<N,E,W>
{
	public boolean isStronglyConnected()
	{
		return this.isConnected();
	}

	public boolean isWeaklyConnected()
	{
		return false;
	}

	public boolean isPolytree()
	{
		return false;
	}

	@Override
	public Set<Graph<N,E,W>> getConnectedComponents()
	{
		return null;
	}

	@Override
	public int getDegree(N node)
	{
		List<E> adjacentEdges = this.getEdges(node);
		int degree = 0;
		for(E adjacentEdge : adjacentEdges)
		{
			if(adjacentEdge.isLoop())
				degree += 2;
			else
				degree++;
		}
		return degree;
	}
}
