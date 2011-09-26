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

public interface TraversableGraph<
	  	N,
	  	E extends Edge<N,? extends Edge.Endpoint<? extends N>> & TraversableCloud<? extends N, ? extends TraversableCloud.Endpoint<? extends N, ? extends N>>,
	  	AE extends TraversableGraph.Endpoint<Object, N, E>,
	  	NE extends TraversableGraph.NodeEndpoint<N, E>,
	  	EE extends TraversableGraph.EdgeEndpoint<N, E>
	  > extends TraversableCloudGraph<Object, N, E, AE, NE, EE>, Graph<N,E,AE,NE,EE>
{
	interface Endpoint<
		  	T,
		  	N,
		  	E extends Edge<N,? extends Edge.Endpoint<? extends N>> & TraversableCloud<? extends N, ? extends TraversableCloud.Endpoint<? extends N, ? extends N>>
		  >
		  extends TraversableCloudGraph.Endpoint<Object,T,N,E>, Graph.Endpoint<T,N,E>
	{
	};

	interface NodeEndpoint<
		  	N,
		  	E extends Edge<N,? extends Edge.Endpoint<? extends N>> & TraversableCloud<? extends N, ? extends TraversableCloud.Endpoint<? extends N, ? extends N>>
	  > extends TraversableCloudGraph.NodeEndpoint<Object,N,E>, Graph.NodeEndpoint<N,E>, Endpoint<N,N,E>
	{
	};

	interface EdgeEndpoint<
		  	N,
		  	E extends Edge<N,? extends Edge.Endpoint<? extends N>> & TraversableCloud<? extends N, ? extends TraversableCloud.Endpoint<? extends N, ? extends N>>
		> extends TraversableCloudGraph.EdgeEndpoint<Object,N,E>, Graph.EdgeEndpoint<N,E>, Endpoint<E,N,E>
	{
	};
}
