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

public interface PartibleCloudGraph<
	  	A,
	  	N,
	  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>,
	  	AE extends PartibleCloudGraph.Endpoint<A, A, N, E>,
	  	NE extends PartibleCloudGraph.NodeEndpoint<A, N, E>,
	  	EE extends PartibleCloudGraph.EdgeEndpoint<A, N, E>
	  >  extends CloudGraph<A,N,E,AE,NE,EE>, PartibleCloud<A, AE>
{
	interface Endpoint<
		  	P,
		  	T,
		  	N,
		  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>
		  >
		  extends CloudGraph.Endpoint<P,T,N,E>, PartibleCloud.Endpoint<P,T>
	{
	};

	interface NodeEndpoint<
		  	P,
		  	N,
		  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>
	  > extends CloudGraph.NodeEndpoint<P,N,E>, PartibleCloud.Endpoint<P,N>, Endpoint<P,N,N,E>
	{
	};

	interface EdgeEndpoint<
		  	P,
		  	N,
		  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>
		> extends CloudGraph.EdgeEndpoint<P,N,E>, PartibleCloud.Endpoint<P,E>, Endpoint<P,E,N,E>
	{
	};

	void leaveNode(MutableCloudGraph.NodeEndpoint<?,?,?> endpoint) throws InvalidGraphException;
	void leaveNodes(Set<? extends MutableCloudGraph.NodeEndpoint<?,?,?>> endpoint) throws InvalidGraphException;

	void leaveEdge(MutableCloudGraph.EdgeEndpoint<?, ?, ?> endpoint) throws InvalidGraphException;
	void leaveEdges(Set<? extends MutableCloudGraph.EdgeEndpoint<?, ?, ?>> endpoints) throws InvalidGraphException;

	void clear() throws InvalidGraphException;
	void clearEdges() throws InvalidGraphException;

	Map<?, CloudGraph.Endpoint<?, ?, N,E>> leaveAll(final Set<? extends CloudGraph.Endpoint<?,?,?,?>> disconnectEndpoints) throws InvalidGraphException;
}