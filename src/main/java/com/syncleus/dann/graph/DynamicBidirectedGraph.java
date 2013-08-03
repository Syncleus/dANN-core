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
	  	NE extends DynamicBidirectedGraph.NodeEndpoint<N>,
	  	E extends BidirectedEdge<? extends BidirectedEdge.Endpoint<N>>,
	  	EE extends DynamicBidirectedGraph.EdgeEndpoint<E>
	  > extends BidirectedGraph<NE, EE>, DynamicGraph<N, NE, E, EE>, DynamicBidirectedHyperedge<N,NE>
{
	interface Endpoint<
		  	T
		  >
		  extends BidirectedGraph.Endpoint<T>, DynamicGraph.Endpoint<T>, DynamicBidirectedHyperedge.Endpoint<T>
	{
	};

	interface NodeEndpoint<
		  	T
	  > extends BidirectedGraph.NodeEndpoint<T>, DynamicGraph.NodeEndpoint<T>, Endpoint<T>
	{
	};

	interface EdgeEndpoint<
		  	T extends BidirectedEdge<?>
		> extends BidirectedGraph.EdgeEndpoint<T>, DynamicGraph.EdgeEndpoint<T>, Endpoint<T>
	{
	};
}
