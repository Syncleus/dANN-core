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

public abstract class AbstractUndirectedEdge<N, LN extends N, RN extends N> extends AbstractBidirectedEdge<N, LN,RN> implements MixableBidirectedEdge<N, LN,RN>
{
	private static final long serialVersionUID = 83475809132709850L;

	protected AbstractUndirectedEdge(final LN leftNode, final RN rightNode)
	{
		super(leftNode, EndState.NONE, rightNode, EndState.NONE);
	}

	protected AbstractUndirectedEdge(final LN leftNode, final RN rightNode, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(leftNode, EndState.NONE, rightNode, EndState.NONE, allowJoiningMultipleGraphs, contextEnabled);
	}

	@Override
	public List<N> getTraversableNodes(final N node)
	{
		if( this.getLeftNode().equals(node) )
			return Collections.singletonList(this.getRightNode());
		else if( this.getRightNode().equals(node) )
			return Collections.singletonList(this.getLeftNode());
		else
			throw new IllegalArgumentException("node is not one of the end points!");
	}

	@Override
	public final boolean isIntroverted()
	{
		return false;
	}

	@Override
	public final boolean isExtroverted()
	{
		return false;
	}

	@Override
	public final boolean isDirected()
	{
		return false;
	}

	@Override
	public final boolean isHalfEdge()
	{
		return false;
	}

	@Override
	public final boolean isLooseEdge()
	{
		return true;
	}

	@Override
	public final boolean isOrdinaryEdge()
	{
		return false;
	}

	@Override
	protected AbstractUndirectedEdge<N, LN,RN> clone()
	{
		return (AbstractUndirectedEdge<N, LN,RN>) super.clone();
	}
}
