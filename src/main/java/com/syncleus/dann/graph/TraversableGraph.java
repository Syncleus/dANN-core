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
	  	NE extends TraversableGraph.NodeEndpoint<?>,
	  	EE extends TraversableGraph.EdgeEndpoint<?>
	  > extends TraversableCloudGraph<NE, EE>, Graph<NE,EE>
{
	interface Endpoint<
		  	T
		  >
		  extends TraversableCloudGraph.Endpoint<T>, Graph.Endpoint<T>
	{
	};

	interface NodeEndpoint<
		  	T
	  > extends TraversableCloudGraph.NodeEndpoint<T>, Graph.NodeEndpoint<T>, Endpoint<T>
	{
	};

	interface EdgeEndpoint<
		  	T extends Edge<?> & TraversableCloud<?>
		> extends TraversableCloudGraph.EdgeEndpoint<T>, Graph.EdgeEndpoint<T>, Endpoint<T>
	{
	};
}
