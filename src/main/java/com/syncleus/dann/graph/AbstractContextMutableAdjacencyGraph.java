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
import com.syncleus.dann.graph.event.context.*;

public abstract class AbstractContextMutableAdjacencyGraph<
	  	N,
	  	E extends Cloud<N,? extends Cloud.Endpoint<N>>,
	  	NE extends MutableCloudGraph.NodeEndpoint<N, E>,
	  	EE extends MutableCloudGraph.EdgeEndpoint<N, E>
	  >
	  extends AbstractMutableAdjacencyGraph<N, E, NE, EE>
{
	@Override
	public final boolean isContextEnabled()
	{
		return true;
	}

	@Override
	protected void internalJoinNode(MutableCloudGraph.NodeEndpoint<N, E> endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));

		try
		{
			if(endpoint.getTarget() instanceof ContextCloudElement)
				((ContextCloudElement<?,?>)endpoint.getTarget()).changingCloudContext(Collections.singleton(this), null);
			if(endpoint.getTarget() instanceof ContextGraphElement)
				((ContextGraphElement<?,?>)endpoint.getTarget()).changingGraphContext(Collections.singleton(this), null, null);
			if(endpoint.getTarget() instanceof ContextGraphNode)
				((ContextGraphNode<?,?>)endpoint.getTarget()).changingGraphNodeContext(Collections.singleton(this), null);
		}
		catch(RejectedContextException caught)
		{
			throw new InvalidGraphException("could not join node", caught);
		}

		super.internalJoinNode(endpoint);

		if(endpoint.getTarget() instanceof ContextCloudElement)
			((ContextCloudElement<?,?>)endpoint.getTarget()).changedCloudContext(Collections.singleton(endpoint), null);
		if(endpoint.getTarget() instanceof ContextGraphElement)
			((ContextGraphElement<?,?>)endpoint.getTarget()).changedGraphContext(Collections.singleton(endpoint), null, null);
		if(endpoint.getTarget() instanceof ContextGraphNode)
			((ContextGraphNode<?,?>)endpoint.getTarget()).changedGraphNodeContext(Collections.singleton(endpoint), null);
	}

	@Override
	protected void internalLeaveNode(MutableCloudGraph.NodeEndpoint<?, ?> endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));

		try
		{
			if(endpoint.getTarget() instanceof ContextCloudElement)
				((ContextCloudElement<?,?>)endpoint.getTarget()).changingCloudContext(null, Collections.singleton(endpoint));
			if(endpoint.getTarget() instanceof ContextGraphElement)
				((ContextGraphElement<?,?>)endpoint.getTarget()).changingGraphContext(null, null, Collections.singleton(endpoint));
			if(endpoint.getTarget() instanceof ContextGraphNode)
				((ContextGraphNode<?,?>)endpoint.getTarget()).changingGraphNodeContext(null, Collections.singleton(endpoint));
		}
		catch(RejectedContextException caught)
		{
			throw new InvalidGraphException("could not join node", caught);
		}

		super.internalLeaveNode(endpoint);

		if(endpoint.getTarget() instanceof ContextCloudElement)
			((ContextCloudElement<?,?>)endpoint.getTarget()).changedCloudContext(null, Collections.singleton(endpoint));
		if(endpoint.getTarget() instanceof ContextGraphElement)
			((ContextGraphElement<?,?>)endpoint.getTarget()).changedGraphContext(null, null, Collections.singleton(endpoint));
		if(endpoint.getTarget() instanceof ContextGraphNode)
			((ContextGraphNode<?,?>)endpoint.getTarget()).changedGraphNodeContext(null, Collections.singleton(endpoint));
	}

	@Override
	protected void internalJoinEdge(MutableCloudGraph.EdgeEndpoint<N, E> endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));

		try
		{
			if(endpoint.getTarget() instanceof ContextCloudElement)
				((ContextCloudElement<?,?>)endpoint.getTarget()).changingCloudContext(Collections.singleton(this), null);
			if(endpoint.getTarget() instanceof ContextGraphElement)
				((ContextGraphElement<?,?>)endpoint.getTarget()).changingGraphContext(Collections.singleton(this), null, null);
			if(endpoint.getTarget() instanceof ContextGraphEdge)
				((ContextGraphEdge<?,?>)endpoint.getTarget()).changingGraphEdgeContext(Collections.singleton(this), null);
		}
		catch(RejectedContextException caught)
		{
			throw new InvalidGraphException("could not join node", caught);
		}

		super.internalJoinEdge(endpoint);

		if(endpoint.getTarget() instanceof ContextCloudElement)
			((ContextCloudElement<?,?>)endpoint.getTarget()).changedCloudContext(Collections.singleton(endpoint), null);
		if(endpoint.getTarget() instanceof ContextGraphElement)
			((ContextGraphElement<?,?>)endpoint.getTarget()).changedGraphContext(Collections.singleton(endpoint), null, null);
		if(endpoint.getTarget() instanceof ContextGraphEdge)
			((ContextGraphEdge<?,?>)endpoint.getTarget()).changedGraphEdgeContext(Collections.singleton(endpoint), null);
	}

	@Override
	protected void internalLeaveEdge(MutableCloudGraph.EdgeEndpoint<?, ?> endpoint) throws InvalidGraphException
	{
		if(endpoint == null)
			throw new IllegalArgumentException(("endpoint can not be null"));

		try
		{
			if(endpoint.getTarget() instanceof ContextCloudElement)
				((ContextCloudElement<?,?>)endpoint.getTarget()).changingCloudContext(null, Collections.singleton(endpoint));
			if(endpoint.getTarget() instanceof ContextGraphElement)
				((ContextGraphElement<?,?>)endpoint.getTarget()).changingGraphContext(null, null, Collections.singleton(endpoint));
			if(endpoint.getTarget() instanceof ContextGraphEdge)
				((ContextGraphEdge<?,?>)endpoint.getTarget()).changingGraphEdgeContext(null, Collections.singleton(endpoint));
		}
		catch(RejectedContextException caught)
		{
			throw new InvalidGraphException("could not join node", caught);
		}

		super.internalLeaveEdge(endpoint);

		if(endpoint.getTarget() instanceof ContextCloudElement)
			((ContextCloudElement<?,?>)endpoint.getTarget()).changedCloudContext(null, Collections.singleton(endpoint));
		if(endpoint.getTarget() instanceof ContextGraphElement)
			((ContextGraphElement<?,?>)endpoint.getTarget()).changedGraphContext(null, null, Collections.singleton(endpoint));
		if(endpoint.getTarget() instanceof ContextGraphEdge)
			((ContextGraphEdge<?,?>)endpoint.getTarget()).changedGraphEdgeContext(null, Collections.singleton(endpoint));
	}

	protected abstract class AbstractNodeEndpoint extends AbstractMutableAdjacencyGraph<N,E,NE,EE>.AbstractNodeEndpoint
	{
		protected AbstractNodeEndpoint(final N target)
		{
			super(target);
		}
	};

	protected abstract class AbstractEdgeEndpoint extends AbstractMutableAdjacencyGraph<N,E,NE,EE>.AbstractEdgeEndpoint
	{
		protected AbstractEdgeEndpoint(final E target)
		{
			super(target);
		}
	};
}
