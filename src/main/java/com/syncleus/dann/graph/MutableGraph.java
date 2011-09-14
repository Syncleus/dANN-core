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

import java.util.Map;
import java.util.Set;

public interface MutableGraph<N, E extends Edge<N>> extends AssignableGraph<N,E>
{
	interface NodeEndpoint<ON, MN extends ON, OE extends Edge<? extends ON>> extends AssignableGraph.NodeEndpoint<ON,MN,OE>, MutableHyperEdge.Endpoint<Object, MN>
	{
	};

	interface EdgeEndpoint<ON, OE extends Edge<? extends ON>, ME extends OE> extends AssignableGraph.EdgeEndpoint<ON,OE,ME>, MutableHyperEdge.Endpoint<Object, ME>
	{
	};

	NodeEndpoint<N,? extends N,E> join(final N node) throws InvalidEdgeException;
	Map<N, ? extends NodeEndpoint<N, ? extends N,E>> joinNodes(final Set<? extends N> nodes) throws InvalidEdgeException;
	void leave(final NodeEndpoint<? extends N, ? extends N,? extends E> endPoint) throws InvalidEdgeException;
	void leaveNodes(final Set<? extends NodeEndpoint<? extends N, ? extends N,? extends E>> endPoint) throws InvalidEdgeException;

	EdgeEndpoint<N, E, ? extends E> join(final E edge) throws InvalidEdgeException;
	Map<N, ? extends EdgeEndpoint<N, E, ? extends E>> joinEdges(final Set<? extends E> edges) throws InvalidEdgeException;
	void leave(final EdgeEndpoint<? extends N, ? extends E, ? extends E> endPoint) throws InvalidEdgeException;
	void leaveEdges(final Set<? extends EdgeEndpoint<? extends N, ? extends E, ? extends E>> endPoint) throws InvalidEdgeException;

	void clear() throws InvalidEdgeException;

	Map<? extends Object, ? extends Graph.Endpoint<? extends N, ? extends N>> reconfigure(final Set<? extends N> addNodes, final Set<? extends E> addEdges, final Set<? extends Graph.Endpoint<? extends N, ? extends N>> disconnectEndPoints) throws InvalidEdgeException;
}
