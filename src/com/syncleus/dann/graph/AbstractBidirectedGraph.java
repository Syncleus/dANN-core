/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph;

import java.util.Set;

public abstract class AbstractBidirectedGraph<N, E extends BidirectedEdge<? extends N>, W extends BidirectedWalk<N, E>> extends AbstractGraph<N,E,W> implements BidirectedGraph<N,E,W>
{
	public boolean isStronglyConnected()
	{
		return false;
	}

	public Set<BidirectedGraph<N,E,W>> getStrongComponents()
	{
		return null;
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
}
