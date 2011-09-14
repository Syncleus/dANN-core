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
	  	PA,
	  	N extends PA,
	  	E extends Edge<N,? extends Edge.Endpoint<N>>,
	  	NEP extends MutableGraph.NodeEndpoint<N, E>,
	  	EEP extends MutableGraph.EdgeEndpoint<N, E>
	  > extends AssignableGraph<PA,N,E,NEP,EEP>
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
	void leaveNode(NEP endPoint) throws InvalidGraphException;
	void leaveNodes(Set<NEP> endPoint) throws InvalidGraphException;

	EEP joinEdge(E edge) throws InvalidGraphException;
	Map<N, EEP> joinEdges(Set<? extends E> edges) throws InvalidGraphException;
	void leaveEdge(EEP endPoint) throws InvalidGraphException;
	void leaveEdges(Set<EEP> endPoint) throws InvalidGraphException;

	void clear() throws InvalidGraphException;
	void clearEdges() throws InvalidGraphException;

	Map<PA, ? extends Graph.Endpoint<N,E,PA>> reconfigure(Set<? extends N> addNodes, Set<? extends E> addEdges, final Set<? extends Graph.Endpoint<N,E,? extends PA>> disconnectEndPoints) throws InvalidGraphException;
}
