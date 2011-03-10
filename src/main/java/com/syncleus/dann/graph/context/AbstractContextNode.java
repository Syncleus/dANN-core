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
package com.syncleus.dann.graph.context;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;

public abstract class AbstractContextNode<N, E extends Edge<N>, G extends Graph<N, E>> extends AbstractContextGraphElement<G> implements ContextNode<N, E>
{
	private final Set<E> connectedEdges = new HashSet<E>();
	private final Set<ContextEdge<N, E, G>> contextEdges = new HashSet<ContextEdge<N, E, G>>();

	protected AbstractContextNode(final boolean allowJoiningMultipleGraphs)
	{
		super(allowJoiningMultipleGraphs);
	}

	@Override
	public boolean joiningGraph(final G graph)
	{
		if(super.joiningGraph(graph))
		{
			//notify all context edges that this node has joined a graph
			for(ContextEdge<N, E, G> contextEdge : contextEdges)
				contextEdge.nodeJoiningGraph(graph, (N) this);
			return true;
		}
		else return false;
	}

	@Override
	public boolean leavingGraph(final G graph)
	{
		if( super.leavingGraph(graph) )
		{
			//notify all context edges that this node is leaving a graph
			for(ContextEdge<N, E, G> contextEdge : contextEdges)
				contextEdge.nodeLeavingGraph(graph, (N) this);
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean connectingEdge(final E edge)
	{
		if (edge == null)
			throw new IllegalArgumentException("edge can not be null");

		connectedEdges.add(edge);
		if (edge instanceof ContextEdge)
			contextEdges.add((ContextEdge)edge);
		return true;
	}

	@Override
	public boolean disconnectingEdge(final E edge)
	{
		if( edge == null )
			throw new IllegalArgumentException("edge can not be null");

		//remove all references to this edge
		connectedEdges.remove(edge);
		contextEdges.remove(edge);
		return true;
	}

	public final Set<E> getConnectedEdges()
	{
		return Collections.unmodifiableSet(connectedEdges);
	}
}
