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

public interface DynamicDirectedGraph<
	  	N,
	  	E extends DirectedEdge<N,? extends BidirectedEdge.Endpoint<? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>,
	  	AE extends DynamicDirectedGraph.Endpoint<Object, N, E>,
	  	NE extends DynamicDirectedGraph.NodeEndpoint<N, E>,
	  	EE extends DynamicDirectedGraph.EdgeEndpoint<N, E>
	  > extends DirectedGraph<N, E, AE, NE, EE>, DynamicBidirectedGraph<N, E, AE, NE, EE>
{
	interface Endpoint<
		  	T,
		  	N,
		  	E extends DirectedEdge<N,? extends BidirectedEdge.Endpoint<? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>
		  >
		  extends DirectedGraph.Endpoint<T,N,E>, DynamicBidirectedGraph.Endpoint<T,N,E>
	{
	};

	interface NodeEndpoint<
		  	N,
		  	E extends DirectedEdge<N,? extends BidirectedEdge.Endpoint<? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>
	  > extends DirectedGraph.NodeEndpoint<N,E>, DynamicBidirectedGraph.NodeEndpoint<N,E>, Endpoint<N,N,E>
	{
	};

	interface EdgeEndpoint<
		  	N,
		  	E extends DirectedEdge<N,? extends BidirectedEdge.Endpoint<? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>
		> extends DirectedGraph.EdgeEndpoint<N,E>, DynamicBidirectedGraph.EdgeEndpoint<N,E>, Endpoint<E,N,E>
	{
	};
}