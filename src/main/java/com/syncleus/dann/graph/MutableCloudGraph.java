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

public interface MutableCloudGraph<
	  	A,
	  	N,
	  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>,
	  	AE extends MutableCloudGraph.Endpoint<A, A, N, E>,
	  	NE extends MutableCloudGraph.NodeEndpoint<A, N, E>,
	  	EE extends MutableCloudGraph.EdgeEndpoint<A, N, E>
	  >  extends JoinableCloudGraph<A,N,E,AE,NE,EE>, PartibleCloudGraph<A,N,E,AE,NE,EE>, AssignableCloudGraph<A,N,E,AE,NE,EE>, MutableCloud<A,AE>
{
	interface Endpoint<
		  	P,
		  	T,
		  	N,
		  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>
		  >
		  extends JoinableCloudGraph.Endpoint<P,T,N,E>, PartibleCloudGraph.Endpoint<P,T,N,E>, AssignableCloudGraph.Endpoint<P,T,N,E>, MutableCloud.Endpoint<P,T>
	{
	};

	interface NodeEndpoint<
		  	P,
		  	N,
		  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>
	  > extends JoinableCloudGraph.NodeEndpoint<P,N,E>, PartibleCloudGraph.NodeEndpoint<P,N,E>, AssignableCloudGraph.NodeEndpoint<P,N,E>, Endpoint<P,N,N,E>
	{
	};

	interface EdgeEndpoint<
		  	P,
		  	N,
		  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>
		> extends JoinableCloudGraph.EdgeEndpoint<P,N,E>, PartibleCloudGraph.EdgeEndpoint<P,N,E>, AssignableCloudGraph.EdgeEndpoint<P,N,E>, Endpoint<P,E,N,E>
	{
	};

	Map<?, CloudGraph.Endpoint<?, ?, N,E>> reconfigure(Set<? extends N> addNodes, Set<? extends E> addEdges, final Set<? extends CloudGraph.Endpoint<?,?,?,?>> disconnectEndpoints) throws InvalidGraphException;
}
