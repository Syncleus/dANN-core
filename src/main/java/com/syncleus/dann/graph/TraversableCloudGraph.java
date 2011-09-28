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

public interface TraversableCloudGraph<
	  	NE extends TraversableCloudGraph.NodeEndpoint<?>,
	  	EE extends TraversableCloudGraph.EdgeEndpoint<? extends TraversableCloud<?>>
	  >  extends CloudGraph<NE,EE>, TraversableCloud<NE>
{
	interface Endpoint<
		  	T
		  >
		  extends CloudGraph.Endpoint<T>, TraversableCloud.Endpoint<T>
	{
	};

	interface NodeEndpoint<
		  	T
	  > extends CloudGraph.NodeEndpoint<T>, Endpoint<T>
	{
	};

	interface EdgeEndpoint<
		  	T extends TraversableCloud<?>
		> extends CloudGraph.EdgeEndpoint<T>, Endpoint<T>
	{
	};


	Set<EE> getTraversableEdgesFrom(Endpoint<?> source);
	Set<EE> getTraversableEdgesTo(Endpoint<?> destination);

	/**
	 * Get a list of all reachable nodes adjacent to node. All edges connected to
	 * node and is traversable from node will have its destination node(s) added to
	 * the returned list. node itself will appear in the list once for every loop.
	 * If there are multiple edges connecting node with a particular end point then
	 * the end point will appear multiple times in the list, once for each hop to
	 * the end point.
	 *
	 * @param destination The whose traversable neighbors are to be returned.
	 * @return A list of all nodes adjacent to the specified node and traversable
	 *         from the spevified node, empty set if the node has no edges.
	 * @since 2.0
	 */
	EndpointSets<NE,EE> getTraversableAdjacentTo(Endpoint<?> destination);

	/**
	 * Get a set of all edges which you can traverse from node. Of course node will
	 * always be an end point for each edge returned. Throws an
	 * IllegalArgumentException if node is not in the graph.
	 *
	 * @param destination edges returned will be traversable from this node.
	 * @return An unmodifiable set of all edges that can be traversed from node.
	 * @since 2.0
	 */
	EndpointSets<NE,EE> getTraversableAdjacentFrom(Endpoint<?> destination);
}
