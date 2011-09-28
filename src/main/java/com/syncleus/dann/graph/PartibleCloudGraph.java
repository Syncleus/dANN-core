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

import java.util.Set;

public interface PartibleCloudGraph<
	  	NE extends PartibleCloudGraph.NodeEndpoint<?>,
	  	EE extends PartibleCloudGraph.EdgeEndpoint<? extends Cloud<?>>
	  >  extends CloudGraph<NE,EE>, PartibleCloud<NE>
{
	interface Endpoint<
		  	T
		  >
		  extends CloudGraph.Endpoint<T>, PartibleCloud.Endpoint<T>
	{
	};

	interface NodeEndpoint<
		  	T
	  > extends CloudGraph.NodeEndpoint<T>, PartibleCloud.Endpoint<T>, Endpoint<T>
	{
	};

	interface EdgeEndpoint<
		  	T extends Cloud<?>
		> extends CloudGraph.EdgeEndpoint<T>, PartibleCloud.Endpoint<T>, Endpoint<T>
	{
	};

	void leaveEdge(EdgeEndpoint<?> endpoint) throws InvalidGraphException;
	void leaveEdges(Set<? extends EdgeEndpoint<?>> endpoints) throws InvalidGraphException;
	void clearEdges() throws InvalidGraphException;

	void leaveAll(final Set<? extends Endpoint<?>> disconnectEndpoints) throws InvalidGraphException;
	Set<EE> recursiveLeaveAll(final Set<? extends Endpoint<?>> disconnectEndpoints) throws InvalidGraphException;
	void clearAll() throws InvalidGraphException;
}