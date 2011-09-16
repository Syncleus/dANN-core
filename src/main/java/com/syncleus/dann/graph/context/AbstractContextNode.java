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
import com.syncleus.dann.graph.Cloud;
import com.syncleus.dann.graph.Graph;

public abstract class AbstractContextNode<N, E extends Cloud<N>, G extends Graph<N, E>> extends AbstractContextGraphElement<G> implements ContextNode<N, E>
{
	private final Set<E> connectedEdges = new HashSet<E>();

	protected AbstractContextNode(final boolean allowJoiningMultipleGraphs)
	{
		super(allowJoiningMultipleGraphs);
	}

	public void changingConnectedEdges(Set<E> connectingEdges, Set<E> disconnectingEdges) throws RejectedContextException
	{
		if( ((connectingEdges == null)||(connectingEdges.isEmpty())) && ((disconnectingEdges == null)||(disconnectingEdges.isEmpty())) )
			throw new IllegalArgumentException("No edges specified for joining or leaving!");

		if( (connectingEdges != null) && (!connectingEdges.isEmpty()) )
		{
			if( connectingEdges.contains(null) )
				throw new IllegalArgumentException("connectingEdges can not have a null element!");
		}
		if( (disconnectingEdges != null) && (!disconnectingEdges.isEmpty()) )
		{
			if( disconnectingEdges.contains(null) )
				throw new IllegalArgumentException("disconnectingEdges can not have a null element!");

			if( !this.connectedEdges.containsAll(disconnectingEdges) )
				throw new IllegalArgumentException("One of the graphs being left has not been joined");
		}
	}

	public void changedConnectedEdges(Set<E> newConnectedEdges, Set<E> newDisconnectedEdges)
	{
		if( ((newConnectedEdges == null)||(newConnectedEdges.isEmpty())) && ((newDisconnectedEdges == null)||(newDisconnectedEdges.isEmpty())) )
			throw new IllegalArgumentException("No edges specified for joining or leaving!");

		if( (newConnectedEdges != null) && (!newConnectedEdges.isEmpty()) )
		{
			if( newConnectedEdges.contains(null) )
				throw new IllegalArgumentException("newConnectedEdges can not have a null element!");

			this.connectedEdges.addAll(newConnectedEdges);
		}

		if( (newDisconnectedEdges != null) && (!newDisconnectedEdges.isEmpty()) )
		{
			if( newDisconnectedEdges.contains(null) )
				throw new IllegalArgumentException("newDisconnectedEdges can not have a null element!");

			this.connectedEdges.removeAll(newDisconnectedEdges);
		}
	}

	protected final Set<E> getConnectedEdges()
	{
		return Collections.unmodifiableSet(connectedEdges);
	}
}
