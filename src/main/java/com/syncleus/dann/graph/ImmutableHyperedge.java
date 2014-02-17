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
import java.util.List;

public final class ImmutableHyperedge<N> extends AbstractHyperedge<N>
{
	private static final long serialVersionUID = -3657973823101515199L;

	public ImmutableHyperedge(final Collection<N> nodes)
	{
		super(nodes);
	}

	public ImmutableHyperedge(final N... nodes)
	{
		super(nodes);
	}

	public ImmutableHyperedge(final List<N> nodes, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(nodes, allowJoiningMultipleGraphs, contextEnabled);
	}

	public ImmutableHyperedge(final boolean allowJoiningMultipleGraphs, final boolean contextEnabled, final N... nodes)
	{
		super(allowJoiningMultipleGraphs, contextEnabled, nodes);
	}

	@Override
	public ImmutableHyperedge<N> connect(final N node)
	{
		return (ImmutableHyperedge<N>) super.connect(node);
	}

	@Override
	public ImmutableHyperedge<N> connect(final List<N> nodes)
	{
		return (ImmutableHyperedge<N>) super.connect(nodes);
	}

	@Override
	public ImmutableHyperedge<N> disconnect(final N node)
	{
		return (ImmutableHyperedge<N>) super.disconnect(node);
	}

	@Override
	public ImmutableHyperedge<N> disconnect(final List<N> nodes)
	{
		return (ImmutableHyperedge<N>) super.disconnect(nodes);
	}

}
