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
	  	NEP extends MutableGraph.NodeEndpoint<N, E>,
	  	EEP extends MutableGraph.EdgeEndpoint<N, E>
	  >  extends CloudGraph<N,E,NEP,EEP>
{

	interface NodeEndpoint<
		  ON,
		  OE extends Cloud<ON,? extends Cloud.Endpoint<? extends ON>>
	  > extends CloudGraph.NodeEndpoint<ON,OE>, PartibleCloud.Endpoint<ON>
	{
	};

	interface EdgeEndpoint<
		  ON,
		  OE extends Cloud<ON,? extends Cloud.Endpoint<? extends ON>>
	  > extends CloudGraph.EdgeEndpoint<ON,OE>, PartibleCloud.Endpoint<OE>
	{
	};

	NE joinNode(N node) throws InvalidGraphException;
	Map<N, NE> joinNodes(Set<? extends N> nodes) throws InvalidGraphException;
	Map<N, Set<NE>> joinNodes(Map<? extends N,? extends Integer> nodes) throws InvalidGraphException;
	void leaveNode(MutableGraph.NodeEndpoint<?,?> endpoint) throws InvalidGraphException;
	void leaveNodes(Set<? extends MutableGraph.NodeEndpoint<?,?>> endpoint) throws InvalidGraphException;

	EE joinEdge(E edge) throws InvalidGraphException;
	Map<E, EE> joinEdges(Set<? extends E> edges) throws InvalidGraphException;
	Map<E, Set<EE>> joinEdges(Map<? extends E,? extends Integer> edges) throws InvalidGraphException;
	void leaveEdge(MutableGraph.EdgeEndpoint<?, ?> endpoint) throws InvalidGraphException;
	void leaveEdges(Set<? extends MutableGraph.EdgeEndpoint<?, ?>> endpoints) throws InvalidGraphException;

	void clear() throws InvalidGraphException;
	void clearEdges() throws InvalidGraphException;

	Map<?, CloudGraph.Endpoint<?, N,E>> reconfigure(Set<? extends N> addNodes, Set<? extends E> addEdges, final Set<? extends CloudGraph.Endpoint<?,?,?>> disconnectEndpoints) throws InvalidGraphException;
}
