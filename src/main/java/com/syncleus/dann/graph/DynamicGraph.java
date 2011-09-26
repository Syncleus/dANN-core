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

public interface DynamicGraph<
	  	N,
	  	E extends Edge<N,? extends Edge.Endpoint<? extends N>> & TraversableCloud<? extends N, ? extends TraversableCloud.Endpoint<? extends N, ? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>,
	  	AE extends DynamicGraph.Endpoint<Object, N, E>,
	  	NE extends DynamicGraph.NodeEndpoint<N, E>,
	  	EE extends DynamicGraph.EdgeEndpoint<N, E>
	  > extends DynamicCloudGraph<Object, N, E, AE, NE, EE>, TraversableGraph<N,E,AE,NE,EE>, MutableGraph<N,E,AE,NE,EE>
{
	interface Endpoint<
		  	T,
		  	N,
		  	E extends Edge<N,? extends Edge.Endpoint<? extends N>> & TraversableCloud<? extends N, ? extends TraversableCloud.Endpoint<? extends N, ? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>
		  >
		  extends DynamicCloudGraph.Endpoint<Object,T,N,E>, TraversableGraph.Endpoint<T,N,E>, MutableGraph.Endpoint<T,N,E>
	{
	};

	interface NodeEndpoint<
		  	N,
		  	E extends Edge<N,? extends Edge.Endpoint<? extends N>> & TraversableCloud<? extends N, ? extends TraversableCloud.Endpoint<? extends N, ? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>
	  > extends DynamicCloudGraph.NodeEndpoint<Object,N,E>, TraversableGraph.NodeEndpoint<N,E>, MutableGraph.NodeEndpoint<N,E>, Endpoint<N,N,E>
	{
	};

	interface EdgeEndpoint<
		  	N,
		  	E extends Edge<N,? extends Edge.Endpoint<? extends N>> & TraversableCloud<? extends N, ? extends TraversableCloud.Endpoint<? extends N, ? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>
		> extends DynamicCloudGraph.EdgeEndpoint<Object,N,E>, TraversableGraph.EdgeEndpoint<N,E>, MutableGraph.EdgeEndpoint<N,E>, Endpoint<E,N,E>
	{
	};
}