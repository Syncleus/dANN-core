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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractUndirectedEdge<N> extends AbstractBidirectedEdge<N> implements UndirectedEdge<N>
{
	private static final long serialVersionUID = 83475809132709850L;

	protected AbstractUndirectedEdge(final N leftNode, final N rightNode)
	{
		super(leftNode, EndState.NONE, rightNode, EndState.NONE);
	}

	protected AbstractUndirectedEdge(final N leftNode, final N rightNode, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(leftNode, EndState.NONE, rightNode, EndState.NONE, allowJoiningMultipleGraphs, contextEnabled);
	}

	@Override
	public Collection<N> getTraversableNodes(final N node)
	{
		if( this.getLeftEndpoint().equals(node) )
			return Collections.singletonList(this.getRightEndpoint());
		else if( this.getRightEndpoint().equals(node) )
			return Collections.singletonList(this.getLeftEndpoint());
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
	public AbstractUndirectedEdge<N> disconnect(final N node)
	{
		if( node == null )
			throw new IllegalArgumentException("node can not be null");
		if( !this.getEndpoints().contains(node) )
			throw new IllegalArgumentException("node is not currently connected to");
		return (AbstractUndirectedEdge<N>) this.remove(node);
	}

	@Override
	public AbstractUndirectedEdge<N> disconnect(final List<N> nodes)
	{
		if( nodes == null )
			throw new IllegalArgumentException("node can not be null");
		if( !this.getEndpoints().containsAll(nodes) )
			throw new IllegalArgumentException("node is not currently connected to");
		return (AbstractUndirectedEdge<N>) this.remove(nodes);
	}

}
