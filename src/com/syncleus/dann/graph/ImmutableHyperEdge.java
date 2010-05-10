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

public class ImmutableHyperEdge<N> extends AbstractEdge<N> implements HyperEdge<N>
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

	public List<N> getTraversableNodes(final N node)
	{
		final List<N> traversableNodes = new ArrayList<N>(this.getNodes());
		if( !traversableNodes.remove(node) )
			throw new IllegalArgumentException("node is not one of the end points!");
		return Collections.unmodifiableList(traversableNodes);
	}

	public int getDegree()
	{
		return 0;
	}

	public boolean isSymmetric(final HyperEdge symmetricEdge)
	{
		return false;
	}

	public ImmutableHyperEdge<N> connect(final N node)
	{
		if( node == null )
			throw new IllegalArgumentException("node can not be null");
		if( this.getNodes().contains(node) )
			throw new IllegalArgumentException("node is already connected");
		return (ImmutableHyperEdge<N>) this.add(node);
	}

	public ImmutableHyperEdge<N> connect(final List<N> nodes)
	{
		if( nodes == null )
			throw new IllegalArgumentException("node can not be null");
		for(final N node : nodes)
			if( this.getNodes().contains(node) )
				throw new IllegalArgumentException("node is already connected");
		return (ImmutableHyperEdge<N>) this.add(nodes);
	}

	public ImmutableHyperEdge<N> disconnect(final N node)
	{
		if( node == null )
			throw new IllegalArgumentException("node can not be null");
		if( !this.getNodes().contains(node) )
			throw new IllegalArgumentException("node is not currently connected to");
		return (ImmutableHyperEdge<N>) this.remove(node);
	}

	public ImmutableHyperEdge<N> disconnect(final List<N> nodes)
	{
		if( nodes == null )
			throw new IllegalArgumentException("node can not be null");
		if( !this.getNodes().containsAll(nodes) )
			throw new IllegalArgumentException("node is not currently connected to");
		return (ImmutableHyperEdge<N>) this.remove(nodes);
	}

	@Override
	public ImmutableHyperEdge<N> clone()
	{
		return (ImmutableHyperEdge<N>) super.clone();
	}
}
