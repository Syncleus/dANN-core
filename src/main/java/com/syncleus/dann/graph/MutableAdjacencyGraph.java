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

public class MutableAdjacencyGraph<
	  	PA,
	  	N extends PA,
	  	E extends Edge<N,? extends Edge.Endpoint<N>>,
	  	NEP extends MutableGraph.NodeEndpoint<N, E>,
	  	EEP extends MutableGraph.EdgeEndpoint<N, E>
	  > extends AbstractAdjacencyGraph<PA, N, E, NEP, EEP> implements MutableGraph<PA, N, E, NEP, EEP>
{
	private static final long serialVersionUID = -4613327727609060678L;

	@Override
	protected Set<EdgeEndpoint<N, E>> getAdjacentEdgeEndPoint(Graph.NodeEndpoint<N, E> nodeEndPoint)
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public NEP joinNode(N node) throws InvalidGraphException
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Map<N, NEP> joinNodes(Set<? extends N> nodes) throws InvalidGraphException
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void leaveNode(NEP endPoint) throws InvalidGraphException
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void leaveNodes(Set<NEP> endPoint) throws InvalidGraphException
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public EEP joinEdge(E edge) throws InvalidGraphException
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Map<N, EEP> joinEdges(Set<? extends E> edges) throws InvalidGraphException
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void leaveEdge(EEP endPoint) throws InvalidGraphException
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void leaveEdges(Set<EEP> endPoint) throws InvalidGraphException
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void clear() throws InvalidGraphException
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void clearEdges() throws InvalidGraphException
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Map<PA, ? extends Graph.Endpoint<N, E, PA>> reconfigure(Set<? extends N> addNodes, Set<? extends E> addEdges, Set<? extends Graph.Endpoint<N, E, ? extends PA>> disconnectEndPoints) throws InvalidGraphException
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Set<EEP> getEdgeEndpoints()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Set<NEP> getNodeEndpoints()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isContextEnabled()
	{
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
