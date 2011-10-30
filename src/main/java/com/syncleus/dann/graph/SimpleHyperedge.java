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

public class SimpleHyperedge<N> extends AbstractHyperedge<N> implements MutableHyperedge<N>
{
	private static final long serialVersionUID = -3657973823101515199L;
	private final boolean contextEnabled;
	private final Set<SimpleEndpoint> endPoints;

	public SimpleHyperedge()
	{
		this(false);
	}

	public SimpleHyperedge(final Set<N> nodes)
	{
		this(nodes, false);
	}

	public SimpleHyperedge(final N... nodes)
	{
		this(false, nodes);
	}

	public SimpleHyperedge(final boolean contextEnabled)
	{
		super();
		this.contextEnabled = contextEnabled;

		this.endPoints = new HashSet<SimpleEndpoint>();
	}

	public SimpleHyperedge(final Set<N> nodes, final boolean contextEnabled)
	{
		super();
		this.contextEnabled = contextEnabled;

		this.endPoints = new HashSet<SimpleEndpoint>(nodes.size());
		for(N node : nodes)
			this.endPoints.add(new SimpleEndpoint(node));
	}

	public SimpleHyperedge(final boolean contextEnabled, final N... nodes)
	{
		super();
		this.contextEnabled = contextEnabled;

		this.endPoints = new HashSet<SimpleEndpoint>(nodes.length);
		for(N node : nodes)
			this.endPoints.add(new SimpleEndpoint(node));
	}

	@Override
	public boolean isContextEnabled()
	{
		return false;
	}

/*
	@Override
	public SimpleHyperedge<N> join(final N node)
	{
		return (SimpleHyperedge<N>) super.join(node);
	}

	@Override
	public SimpleHyperedge<N> join(final List<N> nodes)
	{
		return (SimpleHyperedge<N>) super.join(nodes);
	}
*/

	@Override
	protected SimpleHyperedge<N> clone()
	{
		return (SimpleHyperedge<N>) super.clone();
	}

	@Override
	public MutableHyperedge.Endpoint<N, N> join(N node) throws InvalidEdgeException
	{
	}

	@Override
	public Map<N, MutableHyperedge.Endpoint<N, N>> join(Set<N> nodes) throws InvalidEdgeException
	{
	}

	@Override
	public void leave(MutableHyperedge.Endpoint<N, N> nnEndPoint) throws InvalidEdgeException
	{
	}

	@Override
	public void leave(Set<MutableHyperedge.Endpoint<N, N>> endPoint) throws InvalidEdgeException
	{
	}

	@Override
	public Map<N, MutableHyperedge.Endpoint<N, N>> reconfigure(Set<N> connectNodes, Set<MutableHyperedge.Endpoint<N, N>> disconnectEndPoints) throws InvalidEdgeException
	{
	}

	@Override
	public Set<Endpoint<N, N>> getEndpoints()
	{
	}

	private class SimpleEndpoint extends AbstractEndpoint<N,N> implements MutableHyperedge.Endpoint<N, N>
	{
		public SimpleEndpoint()
		{
			super();
		}

		public SimpleEndpoint(N node)
		{
			super(node);
		}

		@Override
		public Set getTraversableNeighborsTo()
		{
		}

		@Override
		public Set getTraversableNeighborsFrom()
		{
		}

		@Override
		public boolean isTraversable()
		{
		}

		@Override
		public boolean isTraversable(Endpoint destination)
		{
		}

		@Override
		public boolean isTraversable(Object destination)
		{
		}
	};
}
