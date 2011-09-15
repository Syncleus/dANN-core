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

public final class MutableAdjacencyGraph<
	  	N,
	  	E extends Edge<N,? extends Edge.Endpoint<N>>
	  >
	  extends AbstractMutableAdjacencyGraph<N, E, MutableGraph.NodeEndpoint<N, E>, MutableGraph.EdgeEndpoint<N, E>>
	  implements MutableGraph<N, E, MutableGraph.NodeEndpoint<N, E>, MutableGraph.EdgeEndpoint<N, E>>
{
	private static final long serialVersionUID = -4613327727609060678L;

	@Override
	public boolean isContextEnabled()
	{
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected MutableGraph.NodeEndpoint<N, E> createNodeEndpoint(N node) throws InvalidGraphException
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected MutableGraph.EdgeEndpoint<N, E> createEdgeEndpoint(E node) throws InvalidGraphException
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
