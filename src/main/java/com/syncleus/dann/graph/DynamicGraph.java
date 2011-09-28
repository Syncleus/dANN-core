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
	  	NE extends DynamicGraph.NodeEndpoint<N>,
		E extends Edge<? extends Edge.Endpoint<N>> & TraversableCloud<? extends TraversableCloud.Endpoint<N>>,
	  	EE extends DynamicGraph.EdgeEndpoint<E>
	  > extends DynamicCloudGraph<N, NE, E, EE>, TraversableGraph<NE,EE>, MutableGraph<N,NE,E,EE>
{
	interface Endpoint<
		  	T
		  >
		  extends DynamicCloudGraph.Endpoint<T>, TraversableGraph.Endpoint<T>, MutableGraph.Endpoint<T>
	{
	};

	interface NodeEndpoint<
		  	T
	  > extends DynamicCloudGraph.NodeEndpoint<T>, TraversableGraph.NodeEndpoint<T>, MutableGraph.NodeEndpoint<T>, Endpoint<T>
	{
	};

	interface EdgeEndpoint<
		  	T extends Edge<?> & TraversableCloud<?>
		> extends DynamicCloudGraph.EdgeEndpoint<T>, TraversableGraph.EdgeEndpoint<T>, MutableGraph.EdgeEndpoint<T>, Endpoint<T>
	{
	};
}