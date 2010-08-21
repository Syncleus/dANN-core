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

public abstract class AbstractContextNode<N, E extends Edge<N>> extends AbstractContextGraphElement<N,E> implements ContextGraphElement<N,E>, ContextNode<N,E>
{
	private final Set<E> connectedEdges = new HashSet<E>();
	private final Set<ContextEdge> contextEdges = new HashSet<ContextEdge>();

	protected AbstractContextNode(final boolean allowJoiningMultipleGraphs)
	{
		super(allowJoiningMultipleGraphs);
	}

	@Override
	public boolean joiningGraph(Graph<N, E> graph)
	{
		if(super.joiningGraph(graph))
		{
			//notify all context edges that this node has joined a graph
			for(ContextEdge contextEdge : this.contextEdges)
				contextEdge.nodeJoiningGraph(graph, this);
			return true;
		}
		else return false;
	}

	@Override
	public boolean leavingGraph(Graph<N, E> graph)
	{
		if( super.leavingGraph(graph) )
		{
			//notify all context edges that this node is leaving a graph
			for(ContextEdge contextEdge : this.contextEdges)
				contextEdge.nodeLeavingGraph(graph, this);
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean connectingEdge(E edge)
	{
		if( edge == null )
			throw new IllegalArgumentException("edge can not be null");

		this.connectedEdges.add(edge);
		if(edge instanceof ContextEdge)
			this.contextEdges.add((ContextEdge)edge);
		return true;
	}

	@Override
	public boolean disconnectingEdge(E edge)
	{
		if( edge == null )
			throw new IllegalArgumentException("edge can not be null");

		//remove all refrences to thsi edge
		this.connectedEdges.remove(edge);
		this.contextEdges.remove(edge);
		return true;
	}

	public final Set<E> getConnectedEdges()
	{
		return Collections.unmodifiableSet(connectedEdges);
	}
}
