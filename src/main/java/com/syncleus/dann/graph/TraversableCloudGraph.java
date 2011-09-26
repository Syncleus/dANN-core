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

public interface TraversableCloudGraph<
	  	A,
	  	N,
	  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>,
	  	AE extends TraversableCloudGraph.Endpoint<A, A, N, E>,
	  	NE extends TraversableCloudGraph.NodeEndpoint<A, N, E>,
	  	EE extends TraversableCloudGraph.EdgeEndpoint<A, N, E>
	  >  extends CloudGraph<A,N,E,AE,NE,EE>, TraversableCloud<A, AE>
{
	interface Endpoint<
		  	P,
		  	T,
		  	N,
		  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>
		  >
		  extends CloudGraph.Endpoint<P,T,N,E>, TraversableCloud.Endpoint<P,T>
	{
	};

	interface NodeEndpoint<
		  	P,
		  	N,
		  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>
	  > extends CloudGraph.NodeEndpoint<P,N,E>, TraversableCloud.Endpoint<P,N>, Endpoint<P,N,N,E>
	{
	};

	interface EdgeEndpoint<
		  	P,
		  	N,
		  	E extends Cloud<N,? extends Cloud.Endpoint<? extends N, ? extends N>>
		> extends CloudGraph.EdgeEndpoint<P,N,E>, TraversableCloud.Endpoint<P,E>, Endpoint<P,E,N,E>
	{
	};
}
