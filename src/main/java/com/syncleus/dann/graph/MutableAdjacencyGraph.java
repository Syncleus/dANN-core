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

public class MutableAdjacencyGraph<N, E extends Edge<N>> extends AbstractAdjacencyGraph<N, E> implements MutableGraph<N, E>
{
	private static final long serialVersionUID = -4613327727609060678L;

/*
	public MutableAdjacencyGraph()
	{
		super();
	}

	public MutableAdjacencyGraph(final Graph<N, E> copyGraph)
	{
		super(copyGraph);
	}

	public MutableAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	@Override
	public boolean add(final E newEdge)
	{
		return super.add(newEdge);
	}

	@Override
	public boolean add(final N newNode)
	{
		return super.add(newNode);
	}

	@Override
	public boolean remove(final E edgeToRemove)
	{
		return super.remove(edgeToRemove);
	}

	@Override
	public boolean remove(final N nodeToRemove)
	{
		return super.remove(nodeToRemove);
	}

	@Override
	public boolean clear()
	{
		return super.clear();
	}

	@Override
	protected MutableAdjacencyGraph<N, E> clone()
	{
		return (MutableAdjacencyGraph<N, E>) super.clone();
	}
*/
	final private Map<NodeEndpoint<N,E>,EdgeEndpoint<E, ? extends E>> nodeAdjacency = new HashMap<NodeEndpoint<N, E>, EdgeEndpoint<E, ? extends E>>();

	@Override
	protected Set<EdgeEndpoint<E, ? extends E>> getAdjacentEdgeEndPoint(Graph.NodeEndpoint<? extends N, E> nodeEndPoint)
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Set<? extends EdgeEndpoint<E, ? extends E>> getEdgeEndpoints()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Set<? extends NodeEndpoint<? extends N, E>> getNodeEndpoints()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public MutableHyperEdge.Endpoint<Object, Object> join(Object node) throws InvalidEdgeException
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Map<Object, MutableHyperEdge.Endpoint<Object, Object>> join(Set<Object> nodes) throws InvalidEdgeException
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void leave(MutableHyperEdge.Endpoint<Object, Object> endPoint) throws InvalidEdgeException
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void leave(Set<MutableHyperEdge.Endpoint<Object, Object>> endPoint) throws InvalidEdgeException
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void clear() throws InvalidEdgeException
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Map<Object, MutableHyperEdge.Endpoint<Object, Object>> reconfigure(Set<Object> connectNodes, Set<MutableHyperEdge.Endpoint<Object, Object>> disconnectEndPoints) throws InvalidEdgeException
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean isContextEnabled()
	{
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
