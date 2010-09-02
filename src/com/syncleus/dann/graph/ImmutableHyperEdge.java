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

public final class ImmutableHyperEdge<N> extends AbstractHyperEdge<N> implements HyperEdge<N>
{
	private static final long serialVersionUID = -3657973823101515199L;

	public ImmutableHyperEdge(final List<N> nodes)
	{
		super(nodes);
	}

	public ImmutableHyperEdge(final N... nodes)
	{
		super(nodes);
	}

	public ImmutableHyperEdge(final List<N> nodes, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(nodes, allowJoiningMultipleGraphs, contextEnabled);
	}

	public ImmutableHyperEdge(final boolean allowJoiningMultipleGraphs, final boolean contextEnabled, final N... nodes)
	{
		super(allowJoiningMultipleGraphs, contextEnabled, nodes);
	}

	@Override
	public ImmutableHyperEdge<N> connect(final N node)
	{
		return (ImmutableHyperEdge<N>) super.connect(node);
	}

	@Override
	public ImmutableHyperEdge<N> connect(final List<N> nodes)
	{
		return (ImmutableHyperEdge<N>) super.connect(nodes);
	}

	@Override
	public ImmutableHyperEdge<N> disconnect(final N node)
	{
		return (ImmutableHyperEdge<N>) super.disconnect(node);
	}

	@Override
	public ImmutableHyperEdge<N> disconnect(final List<N> nodes)
	{
		return (ImmutableHyperEdge<N>) super.disconnect(nodes);
	}

	@Override
	public ImmutableHyperEdge<N> clone()
	{
		return (ImmutableHyperEdge<N>) super.clone();
	}
}
