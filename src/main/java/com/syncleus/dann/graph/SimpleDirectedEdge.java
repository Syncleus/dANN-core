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

public class SimpleDirectedEdge<N> extends AbstractDirectedEdge<N, N,N> implements MutableMixableDirectedEdge<N, N,N>
{
	private static final long serialVersionUID = 94357801283489L;

	private final SimpleSourceEndpoint source;
	private final SimpleDestinationEndpoint destination;

	public SimpleDirectedEdge(final N source, final N destination)
	{
		super();
		this.source = new SimpleSourceEndpoint(source);
		this.destination = new SimpleDestinationEndpoint(destination);
	}

	public SimpleDirectedEdge(final N source, final N destination, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super();
		this.source = new SimpleSourceEndpoint(source);
		this.destination = new SimpleDestinationEndpoint(destination);
	}

	@Override
	public boolean isContextEnabled()
	{
		return false;
	}

	@Override
	public AbstractSourceEndpoint getSourceEndPoint()
	{
		return this.source;
	}

	@Override
	public AbstractDestinationEndpoint getDestinationEndPoint()
	{
		return this.destination;
	}

	@Override
	protected SimpleDirectedEdge<N> clone()
	{
		return (SimpleDirectedEdge<N>) super.clone();
	}

	@Override
	public void reassign(N newSourceNode, N newDestinationNode) throws InvalidEdgeException
	{
		return null;
	}

	@Override
	public void invertDirection() throws InvalidEdgeDirectionException
	{
		return null;
	}

	@Override
	public void reassign(N newLeftNode, MixableBidirectedEdge.Endpoint.Direction newLeftDirection, N newRightNode, MixableBidirectedEdge.Endpoint.Direction newRightDirection) throws InvalidEdgeException
	{
		return null;
	}

	private class SimpleSourceEndpoint extends AbstractSourceEndpoint implements MutableMixableDirectedEdge<N,N,N>.Endpoint
	{
		public SimpleSourceEndpoint()
		{
			super();
		}

		public SimpleSourceEndpoint(N node)
		{
			super(node);
		}
	};

	private class SimpleDestinationEndpoint extends AbstractDestinationEndpoint implements MutableMixableDirectedEdge<N,N,N>.Endpoint
	{
		public SimpleDestinationEndpoint()
		{
			super();
		}

		public SimpleDestinationEndpoint(N node)
		{
			super(node);
		}
	};
}
