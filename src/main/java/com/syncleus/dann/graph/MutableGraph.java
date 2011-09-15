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
	  	E extends Edge<? extends N,? extends Edge.Endpoint<? extends N>>,
	  	NEP extends MutableGraph.NodeEndpoint<? extends N, ? extends E>,
	  	EEP extends MutableGraph.EdgeEndpoint<? extends N, ? extends E>
	  > extends AssignableGraph<N,E,NEP,EEP>
{
	interface NodeEndpoint<
		  ON,
		  OE extends Edge<ON,? extends Edge.Endpoint<ON>>
	  > extends AssignableGraph.NodeEndpoint<ON,OE>
	{
	};

	interface EdgeEndpoint<
		  ON,
		  OE extends Edge<ON,? extends Edge.Endpoint<ON>>
	  > extends AssignableGraph.EdgeEndpoint<ON,OE>
	{
	};

	NEP joinNode(N node) throws InvalidGraphException;
	Map<N, NEP> joinNodes(Set<? extends N> nodes) throws InvalidGraphException;
	Map<N, Set<NEP>> joinNodes(Map<? extends N,? extends Integer> nodes) throws InvalidGraphException;
	Set<EEP> leaveNode(MutableGraph.NodeEndpoint<?, ? extends Edge<?,? extends Edge.Endpoint<?>>> endpoint) throws InvalidGraphException;
	Set<EEP> leaveNodes(Set<? extends MutableGraph.NodeEndpoint<?, ? extends Edge<?,? extends Edge.Endpoint<?>>>> endpoint) throws InvalidGraphException;

	EEP joinEdge(E edge) throws InvalidGraphException;
	Map<E, EEP> joinEdges(Set<? extends E> edges) throws InvalidGraphException;
	Map<E, Set<EEP>> joinEdges(Map<? extends E,? extends Integer> edges) throws InvalidGraphException;
	void leaveEdge(MutableGraph.EdgeEndpoint<?, ? extends Edge<?,? extends Edge.Endpoint<?>>> endpoint) throws InvalidGraphException;
	void leaveEdges(Set<? extends MutableGraph.EdgeEndpoint<?, ? extends Edge<?,? extends Edge.Endpoint<?>>>> endpoints) throws InvalidGraphException;

	void clear() throws InvalidGraphException;
	void clearEdges() throws InvalidGraphException;

	Map<?, Graph.Endpoint<N,E,?>> reconfigure(Set<? extends N> addNodes, Set<? extends E> addEdges, final Set<? extends Graph.Endpoint<?,?,?>> disconnectEndpoints) throws InvalidGraphException;
}
