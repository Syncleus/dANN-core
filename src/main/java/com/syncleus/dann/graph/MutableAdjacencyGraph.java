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

public final class MutableAdjacencyGraph<
	  	N,
	  	E extends Cloud<N,? extends Cloud.Endpoint<N>>
	  >
	  extends AbstractContextMutableAdjacencyGraph<N, E, MutableGraph.NodeEndpoint<N, E>, MutableGraph.EdgeEndpoint<N, E>>
{
	private static final long serialVersionUID = -4613327727609060678L;
	private final boolean areNodesUnique;
	private final boolean areEdgesUnique;

	public MutableAdjacencyGraph()
	{
		this(false,true);
	}

	public MutableAdjacencyGraph(final boolean areEdgesUnique)
	{
		this(areEdgesUnique, true);
	}

	public MutableAdjacencyGraph(final boolean areEdgesUnique, final boolean areNodesUnique)
	{
		this.areEdgesUnique = areEdgesUnique;
		this.areNodesUnique = areNodesUnique;
	}

	@Override
	public MutableGraph.NodeEndpoint<N, E> joinNode(N node) throws InvalidGraphException
	{
		final NodeEndpoint endpoint = new NodeEndpoint(node);
		this.internalJoinNode(endpoint);
		return endpoint;
	}

	@Override
	public MutableGraph.EdgeEndpoint<N, E> joinEdge(E edge) throws InvalidGraphException
	{
		final EdgeEndpoint endpoint = new EdgeEndpoint(edge);
		this.internalJoinEdge(endpoint);
		return endpoint;
	}

	protected final class NodeEndpoint extends AbstractContextMutableAdjacencyGraph<N,E,MutableGraph.NodeEndpoint<N, E>,MutableGraph.EdgeEndpoint<N, E>>.AbstractNodeEndpoint
	{
		protected NodeEndpoint(final N target)
		{
			super(target);
		}

		@Override
		protected boolean isTargetEquals()
		{
			return areNodesUnique;
		}
	};

	protected final class EdgeEndpoint extends AbstractContextMutableAdjacencyGraph<N,E,MutableGraph.NodeEndpoint<N, E>,MutableGraph.EdgeEndpoint<N, E>>.AbstractEdgeEndpoint
	{
		protected EdgeEndpoint(final E target)
		{
			super(target);
		}

		@Override
		protected boolean isTargetEquals()
		{
			return areEdgesUnique;
		}
	};
}
