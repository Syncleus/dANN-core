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

public interface JoinableCloudGraph<
	  	N,
	  	NE extends JoinableCloudGraph.NodeEndpoint<N>,
	  	E extends Cloud<? extends Cloud.Endpoint<? extends N>>,
	  	EE extends JoinableCloudGraph.EdgeEndpoint<E>
	  >  extends CloudGraph<NE,EE>, JoinableCloud<N,NE>
{
	interface Endpoint<
		  	T
		  >
		  extends CloudGraph.Endpoint<T>, JoinableCloud.Endpoint<T>
	{
	};

	interface NodeEndpoint<
		  	T
	  > extends CloudGraph.NodeEndpoint<T>, Endpoint<T>
	{
	};

	interface EdgeEndpoint<
		  	T extends Cloud<?>
		> extends CloudGraph.EdgeEndpoint<T>, Endpoint<T>
	{
	};

	EE joinEdge(E edge) throws InvalidGraphException;
	Set<EE> joinEdges(Set<? extends E> edges) throws InvalidGraphException;
	Set<EE> joinEdges(Map<? extends E,? extends Integer> edges) throws InvalidGraphException;

	Endpoints<NE,EE> joinAll(Set<? extends N> addNodes, Set<? extends E> addEdges) throws InvalidGraphException;
	Endpoints<NE,EE> joinAll(Map<? extends N,? extends Integer> addNodes, Map<? extends E,? extends Integer> addEdges) throws InvalidGraphException;
}
