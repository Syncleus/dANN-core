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

import com.syncleus.dann.graph.Graph;

public abstract class AbstractSignalingContextEdge<N,S> extends AbstractContextGraphElement<Graph<N,?>> implements SignalingContextEdge<N,S>
{
	protected AbstractSignalingContextEdge(final boolean allowJoiningMultipleGraphs)
	{
		super(allowJoiningMultipleGraphs);
	}

	protected AbstractSignalingContextEdge()
	{
		super(true);
	}

	/**
	 * This method will retransmit the state to all traversable nodes even if context is disabled.
	 */
	@Override
	public void nodeStateChanged(N node, S newState)
	{
		if( !this.getNodes().contains(node) )
			throw new IllegalArgumentException("node is not an endpoint of this edge");

		for(N traversableNode : this.getTraversableNodes(node))
		{
			if( traversableNode instanceof SignalContextNode)
				((SignalContextNode)traversableNode).neighborNodeStateChanged(this, node, newState);
		}
	}

	public abstract AbstractSignalingContextEdge<N,S> clone(); 
}
