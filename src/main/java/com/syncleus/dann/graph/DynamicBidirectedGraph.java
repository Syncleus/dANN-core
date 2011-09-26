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

public interface DynamicBidirectedGraph<
	  	N,
	  	E extends BidirectedEdge<N,? extends BidirectedEdge.Endpoint<? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>,
	  	AE extends DynamicBidirectedGraph.Endpoint<Object, N, E>,
	  	NE extends DynamicBidirectedGraph.NodeEndpoint<N, E>,
	  	EE extends DynamicBidirectedGraph.EdgeEndpoint<N, E>
	  > extends BidirectedGraph<N, E, AE, NE, EE>, DynamicGraph<N, E, AE, NE, EE>
{
	interface Endpoint<
		  	T,
		  	N,
		  	E extends BidirectedEdge<N,? extends BidirectedEdge.Endpoint<? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>
		  >
		  extends BidirectedGraph.Endpoint<T,N,E>, DynamicGraph.Endpoint<T,N,E>
	{
	};

	interface NodeEndpoint<
		  	N,
		  	E extends BidirectedEdge<N,? extends BidirectedEdge.Endpoint<? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>
	  > extends BidirectedGraph.NodeEndpoint<N,E>, DynamicGraph.NodeEndpoint<N,E>, Endpoint<N,N,E>
	{
	};

	interface EdgeEndpoint<
		  	N,
		  	E extends BidirectedEdge<N,? extends BidirectedEdge.Endpoint<? extends N>> & AssignableCloud<? extends N, ? extends AssignableCloud.Endpoint<? extends N, ? extends N>>
		> extends BidirectedGraph.EdgeEndpoint<N,E>, DynamicGraph.EdgeEndpoint<N,E>, Endpoint<E,N,E>
	{
	};
}
