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

import com.syncleus.dann.UnexpectedDannError;
import com.syncleus.dann.graph.context.AbstractContextGraphElement;
import com.syncleus.dann.graph.context.ContextNode;
import org.apache.log4j.Logger;

public abstract class AbstractTraversableCloud<E extends TraversableCloud.Endpoint<?>> extends AbstractContextGraphElement<Graph<?, ? extends E>> implements TraversableCloud<E>
{
	private static final Logger LOGGER = Logger.getLogger(AbstractTraversableCloud.class);
	private final boolean contextEnabled;
//	private Set<E> endpoints;

	protected AbstractTraversableCloud()
	{
		this(true, true);
	}

	protected AbstractTraversableCloud(final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(allowJoiningMultipleGraphs);
		this.contextEnabled = contextEnabled;
	}

    /*
	protected AbstractTraversableCloud(final Collection<N> ourNodes)
	{
		this(ourNodes, true, true);
	}

	protected AbstractTraversableCloud(final Collection<N> ourNodes, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(allowJoiningMultipleGraphs);
		this.contextEnabled = contextEnabled;

		//make sure each node with context allows us to connect to it
		if(contextEnabled)
		{
			final List<N> nodesCopy = new ArrayList<N>(ourNodes.size());
			for(N ourNode : ourNodes)
			{
				if( this.contextEnabled && ( ourNode instanceof ContextNode ) && ( !((ContextNode)ourNode).connectingEdge(this) ))
					continue;
				nodesCopy.add(ourNode);
			}
			this.endpoints = Collections.unmodifiableList(new ArrayList<N>(nodesCopy));
		}
		else
			this.endpoints = Collections.unmodifiableList(new ArrayList<N>(ourNodes));
	}

	protected AbstractTraversableCloud(final N... ourNodes)
	{
		this(true, true, ourNodes);
	}

	protected AbstractTraversableCloud(final boolean allowJoiningMultipleGraphs, final boolean contextEnabled, final N... ourNodes)
	{
		this(Arrays.asList(ourNodes), allowJoiningMultipleGraphs, contextEnabled);
	}
	*/

	@Override
	public boolean isContextEnabled()
	{
		return this.contextEnabled;
	}
/*
	protected AbstractTraversableCloud<N> add(final N node)
	{
		if( node == null )
			throw new IllegalArgumentException("node can not be null");

		final List<N> newNodes = new ArrayList<N>(this.endpoints);
		newNodes.add(node);

		return createDeepCopy(newNodes);
	}

	protected AbstractTraversableCloud<N> add(final List<N> addNodes)
	{
		if( addNodes == null )
			throw new IllegalArgumentException("node can not be null");
		final List<N> newNodes = new ArrayList<N>(this.endpoints);
		newNodes.addAll(addNodes);

		return createDeepCopy(newNodes);
	}

	protected AbstractTraversableCloud<N> remove(final N node)
	{
		if( node == null )
			throw new IllegalArgumentException("node can not be null");
		if( !this.endpoints.contains(node) )
			throw new IllegalArgumentException("is not an endpoint");

		final List<N> newNodes = new ArrayList<N>(this.endpoints);
		newNodes.remove(node);

		return createDeepCopy(newNodes);
	}

	protected AbstractTraversableCloud<N> remove(final List<N> removeNodes)
	{
		if( removeNodes == null )
			throw new IllegalArgumentException("removeNodes can not be null");
		if( !this.endpoints.containsAll(removeNodes) )
			throw new IllegalArgumentException("removeNodes do not contain all valid end points");
		final List<N> newNodes = new ArrayList<N>(this.endpoints);
		for(final N node : removeNodes)
			newNodes.remove(node);

		return createDeepCopy(newNodes);
	}
*/
/*
	**
	 * Create a deep copy of this edge, but with a new set of endpoints.
	 * @param newNodes the set of endpoints to use instead of the current ones.
	 * @return a deep copy of this edge, but with a new set of endpoints.
	 */
/*
	private AbstractTraversableCloud<N> createDeepCopy(final List<N> newNodes)
	{
		try
		{
			final AbstractTraversableCloud<N> clonedEdge = (AbstractTraversableCloud<N>) super.clone();
			final List<N> clonedNodes = new ArrayList<N>(this.endpoints.size());
			//add each node at a time to the clone considering context
			for(N newNode : newNodes)
			{
				if( this.contextEnabled && (newNode instanceof ContextNode) && ( !((ContextNode)newNode).connectingEdge(clonedEdge) ) )
					continue;
				clonedNodes.add(newNode);
			}
			clonedEdge.endpoints = Collections.unmodifiableList(clonedNodes);
			return clonedEdge;
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("Edge was unexpectidly not cloneable", caught);
			throw new UnexpectedDannError("Edge was unexpectidly not cloneable", caught);
		}
	}
*/

	@Override
	public boolean isTraversable(final E endpoint)
	{
		return (!this.getTraversableEndpoints(endpoint).isEmpty());
	}

    /*
	@Override
	public final Set<E> getEndpoints()
	{
		return this.endpoints;
	}
	*/

	@Override
	public String toString()
	{
		final StringBuilder outString = new StringBuilder(this.getEndpoints().size() * 10);
		for(final E endpoint : this.getEndpoints())
		{
			outString.append(':').append(endpoint);
		}
		return outString.toString();
	}

}
