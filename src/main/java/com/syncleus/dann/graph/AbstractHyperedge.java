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

public abstract class AbstractHyperedge<E extends Hyperedge.Endpoint<?>> extends AbstractCloud<E> implements Hyperedge<E>
{
	private static final long serialVersionUID = -3657973823101515199L;

/*
	protected AbstractHyperedge(final List<N> nodes, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(nodes, allowJoiningMultipleGraphs, contextEnabled);

		if(this.contextEnabled)
		{
			this.nodes = new ArrayList<N>(ourNodes.size());
			try
			{
				for( N node : ourNodes )
				{
					if( node instanceof ContextNode )
						((ContextNode)node).connectingEdge(this);

					this.nodes.add(node);
					if( node instanceof ContextNode )
						((ContextNode)node).connectedEdge(this);
				}
			}
			catch(RejectedContextException caught)
			{
				//we need to remove any connections we established before we bail
				for(N node : this.nodes)
					if( node instanceof ContextNode )
						((ContextNode)node).disconnectedEdge(this);
				throw new InvalidContextException(caught);
			}
		}
		else
			this.nodes = new ArrayList<N>(ourNodes);
	}


	protected AbstractHyperedge(final boolean allowJoiningMultipleGraphs, final boolean contextEnabled, final N... nodes)
	{
		super(allowJoiningMultipleGraphs, contextEnabled, nodes);
	}

	protected void addNode(final N node)
	{
		try
		{
			if( this.contextEnabled && (node instanceof ContextNode) )
				((ContextNode)node).connectingEdge(this);
		}
		catch(RejectedContextException caught)
		{
			throw new InvalidContextException(caught);
		}

		this.nodes.add(node);
		if( this.contextEnabled && (node instanceof ContextNode) )
			((ContextNode)node).connectedEdge(this);
	}

	protected void removeNode(final N node)
	{
		try
		{
			if( this.contextEnabled && (node instanceof ContextNode) )
				((ContextNode)node).disconnectingEdge(this);
		}
		catch(RejectedContextException caught)
		{
			throw new InvalidContextException(caught);
		}

		this.nodes.remove(node);
		if( this.contextEnabled && (node instanceof ContextNode) )
			((ContextNode)node).disconnectedEdge(this);
	}

	@Override
	public List<N> getTraversableNodes(final N node)
	{
		final List<N> traversableNodes = new ArrayList<N>(this.getTargets());
		if( !traversableNodes.remove(node) )
			throw new IllegalArgumentException("node is not one of the end points!");
		return Collections.unmodifiableList(traversableNodes);
	}

	@Override
	public int getDegree()
	{
		return this.getTargets().size();
	}
*/
	// TODO : Implement This!
    @Override
	public boolean isSymmetric(Hyperedge<? extends E> symmetricEdge)
	{
		throw new UnsupportedOperationException("this operation is not yet supported");
	}

	@Override
	protected AbstractHyperedge<E> clone()
	{
		return (AbstractHyperedge<E>) super.clone();
	}

	protected abstract class AbstractEndpoint<T> extends AbstractCloud<E>.AbstractEndpoint<T> implements Hyperedge.Endpoint<T>
	{
		protected AbstractEndpoint()
		{
			super();
		}

		protected AbstractEndpoint(T target)
		{
			super(target);
		}
	};
}
