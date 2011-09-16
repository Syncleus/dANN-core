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

public interface MutableGraph<
	  	N,
	  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N>>,
	  	NE extends MutableGraph.NodeEndpoint<N, E>,
	  	EE extends MutableGraph.EdgeEndpoint<N, E>
	  > extends AssignableGraph<N,E,NE,EE>
{
	interface NodeEndpoint<
		  N,
		  E extends Cloud<N,? extends Cloud.Endpoint<? extends N>>
	  > extends AssignableGraph.NodeEndpoint<N,E>
	{
	};

	interface EdgeEndpoint<
		  ON,
		  OE extends Cloud<ON,? extends Cloud.Endpoint<? extends ON>>
	  > extends AssignableGraph.EdgeEndpoint<ON,OE>
	{
	};

	NE joinNode(N node) throws InvalidGraphException;
	Map<N, NE> joinNodes(Set<? extends N> nodes) throws InvalidGraphException;
	Map<N, Set<NE>> joinNodes(Map<? extends N,? extends Integer> nodes) throws InvalidGraphException;
	Set<EE> leaveNode(MutableGraph.NodeEndpoint<?, ? extends Cloud<?,? extends Cloud.Endpoint<?>>> endpoint) throws InvalidGraphException;
	Set<EE> leaveNodes(Set<? extends MutableGraph.NodeEndpoint<?, ? extends Cloud<?,? extends Cloud.Endpoint<?>>>> endpoint) throws InvalidGraphException;

	EE joinEdge(E edge) throws InvalidGraphException;
	Map<E, EE> joinEdges(Set<? extends E> edges) throws InvalidGraphException;
	Map<E, Set<EE>> joinEdges(Map<? extends E,? extends Integer> edges) throws InvalidGraphException;
	void leaveEdge(MutableGraph.EdgeEndpoint<?, ? extends Cloud<?,? extends Cloud.Endpoint<?>>> endpoint) throws InvalidGraphException;
	void leaveEdges(Set<? extends MutableGraph.EdgeEndpoint<?, ? extends Cloud<?,? extends Cloud.Endpoint<?>>>> endpoints) throws InvalidGraphException;

	void clear() throws InvalidGraphException;
	void clearEdges() throws InvalidGraphException;

	Map<?, Graph.Endpoint<?, N,E>> reconfigure(Set<? extends N> addNodes, Set<? extends E> addEdges, final Set<? extends Graph.Endpoint<?, ?,?>> disconnectEndpoints) throws InvalidGraphException;
}
