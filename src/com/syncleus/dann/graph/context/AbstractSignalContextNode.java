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

import java.util.HashSet;
import java.util.Set;
import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;

public abstract class AbstractSignalContextNode<N, E extends Edge<N>, S> extends AbstractContextNode<N, E, Graph<N,E>> implements SignalContextNode<N, E, S>
{
	private final Set<SignalingContextEdge<N,S>> contextEdges = new HashSet<SignalingContextEdge<N,S>>();
	private transient S state = null;

	protected AbstractSignalContextNode(final boolean allowJoiningMultipleGraphs)
	{
		super(allowJoiningMultipleGraphs);
	}

	protected AbstractSignalContextNode()
	{
		super(true);
	}

	@Override
	public boolean connectingEdge(E edge)
	{
		if( super.connectingEdge(edge) )
		{
			if(edge instanceof SignalingContextEdge)
				this.contextEdges.add((SignalingContextEdge)edge);
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean disconnectingEdge(E edge)
	{
		if( super.disconnectingEdge(edge) )
		{
			if(edge instanceof SignalingContextEdge)
				this.contextEdges.remove(edge);
			return true;
		}
		else
			return false;
	}

	@Override
	public S getState()
	{
		return this.state;
	}

	protected void setState(S state)
	{
		this.state = state;

		//lets notify all edges
		for(SignalingContextEdge edge : this.contextEdges)
		{
			if( edge.isTraversable(this) )
				edge.nodeStateChanged(this, state);
		}
	}
}
