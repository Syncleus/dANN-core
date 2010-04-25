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

/**
 * A type of graph where every edge has exactly 2 end points and may have a
 * directionality at either end point. The two end points at each end do not
 * need to be unique, allowing for loops. Each end point of every edge can have
 * one of three states, no directionality, outward, or inward. Both undirected
 * and directed graphs (as well as a mix of the two) extend from this type of
 * graph. With undirected edges neither end point has directionality. With
 * directed edges both ends have opposite orientation, outward on one end and
 * inward on the other, such that both ends point in the same direction.
 *
 * @author Jeffrey Phillips Freeman
 * @param <N> The class represenging a node.
 * @param <E> The bidirected edge used to connect nodes.
 * @since 2.0
 */
public interface BidirectedGraph<N, E extends BidirectedEdge<N>> extends Graph<N,E>
{
	/**
	 * Get all edges which traverse from the specified node. While the
	 * combination of out edges and in edges will usually result in all the
	 * adacent edges for a node this is not strictly required.
	 *
	 * @param node The source node the returned edges will traverse from.
	 * @return an unmodifiable set of all edges which traverse from the specified
	 * node.
	 * @since 2.0
	 */
	Set<E> getOutEdges(N node);

	/**
	 * Get all edges which traverse to the specified node. While the
	 * combination of out edges and in edges will usually result in all the
	 * adacent edges for a node this is not strictly required.
	 *
	 * @param node The destination node the returned edges will traverse to.
	 * @return an unmodifiable set of all edges which traverse to the specified
	 * node.
	 * @since 2.0
	 */
	Set<E> getInEdges(N node);
	
	/**
	 * Count all edges which traverse from the specified node. While the
	 * combination of out edges and in edges will usually result in all the
	 * adacent edges for a node this is not strictly required.
	 *
	 * @param node The source node the returned edges will traverse from.
	 * @return count of all edges which traverse from the specified node.
	 * @since 2.0
	 */
	int getOutdegree(N node);

	/**
	 * Count all edges which traverse to the specified node. While the
	 * combination of out edges and in edges will usually result in all the
	 * adacent edges for a node this is not strictly required.
	 *
	 * @param node The destination node the edges counted will traverse to.
	 * @return count of all edges which traverse to the specified node.
	 * @since 2.0
	 */
	int getIndegree(N node);

	/**
	 * Determines if the graph is a simple acyclic graph with no more than one
	 * undirected edge between any two nodes, there can be muliple directed
	 * edges so long as the graph remains simple and acyclic. if there are no
	 * edges or nodes this returns true.
	 *
	 * @return true if the graph is a polytree, false otherwise.
	 * @since 2.0
	 */
	boolean isPolytree();
}
