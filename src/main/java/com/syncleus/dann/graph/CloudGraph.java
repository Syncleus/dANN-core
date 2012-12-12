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

import java.util.Collection;
import java.util.Set;

/**
 * Represents a graph as a collection of nodes connected by edges. A graph does
 * not need to contain any nodes or edges however if there is at least one edge
 * then there must be at least one node. There can, however, be one or more
 * nodes with no edges present. Each edge must have 2 or more nodes it connects,
 * however they do not need to be different nodes. The implementation defines if
 * and how a graph can be traversed across nodes and edges.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 * @param <NE> The node type
 * @param <EE> The type of edge for the given node type
 */
public interface CloudGraph<
	  	NE extends CloudGraph.NodeEndpoint<?>,
	  	EE extends CloudGraph.EdgeEndpoint<? extends Cloud<?>>
	  > extends Cloud<NE>
{
	interface Endpoint<
		  	T
		  >
		  extends Cloud.Endpoint<T>
	{
		boolean isNodeEndpoint();
		boolean isEdgeEndpoint();
	};

	interface NodeEndpoint<
		  	T
	  > extends Endpoint<T>
	{
	};

	interface EdgeEndpoint<
		  	T extends Cloud<?>
		> extends Endpoint<T>
	{
	};

	interface Endpoints<NE extends NodeEndpoint<?>,EE extends EdgeEndpoint<? extends Cloud<?>>>
	{
		Set<NE> getNodeEndpoints();
		Set<EE> getEdgeEndpoints();
	};

/*
	Endpoints<NE,EE> getAdjacent(NE endpoint);
	Set<NE> getEdgeAdjacent(EE endpoint);
*/
	Endpoints<NE,EE> getAdjacent(Cloud.Endpoint<?> endpoint);

	boolean areEdgesFinite();

	Set<EE> getEdgeEndpoints();
	Set<EE> getEdgeEndpoints(Cloud<?> cloud);

	boolean containsEdge( Cloud.Endpoint<?> endpoint);
	boolean containsAnyEdges(Collection<? extends Cloud.Endpoint<?>> endpoint);
	boolean containsAllEdges(Collection<? extends Cloud.Endpoint<?>> endpoint);
	boolean containsEdgeTarget(Cloud<?> target);
	boolean containsAnyEdgeTargets(Collection<? extends Cloud<?>> targets);
	boolean containsAllEdgeTargets(Collection<? extends Cloud<?>> targets);
}
