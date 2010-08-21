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
package com.syncleus.dann.graph.topological;

import java.util.Set;
import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;

public interface KnotOptimizedGraph<N, E extends Edge<N>> extends Graph<N, E>
{
	/**
	 * Determines if the specified nodes can be traversed to from outside of the
	 * set, and once the set is entered there is no path to traverse outside of the
	 * set. If the specified nodes each have atleast one traversable edge in the
	 * specified edges, all specified traversable edges go to other nodes in the
	 * knot, no traversable edges connect to a node outside of the knot (even if it
	 * isnt specified, and there is atleast onentraversable edge from a node
	 * outside of the knot to a node in the knot (obviously not specified in
	 * knotedEdges). It is important to note that while the knot is not a maximally
	 * connected component of the graph it is weakly connected amongst itself.
	 *
	 * @param knotedNodes A set of nodes to check if they form a knot.
	 * @return true if the specified nodes can be traversed to from outside of the
	 *         set, and once the set is entered there is no path to traverse
	 *         outside of the set.
	 * @throws IllegalArgumentException if any of the specified nodes or edges are
	 * not in the graph.
	 * @since 2.0
	 */
	boolean isKnot(Set<N> knotedNodes, Set<E> knotedEdges);
	/**
	 * Determines if the specified nodes can be traversed to from outside of the
	 * set, and once the set is entered there is no path to traverse outside of the
	 * set. if the specified nodes each have atleast one traversable edge, all
	 * traversable edges go to other nodes in the knot, no traversable edges
	 * connect to a node outside of the know, and there is atleast one traversable
	 * edge from a node outside of the knot to a node in the knot. It is important
	 * to note that while the knot is not a maximally connected component of the
	 * graph it is weakly connected amongst itself.
	 *
	 * @param knotedNodes A set of nodes to check if they form a knot.
	 * @return true if the specified nodes can be traversed to from outside of the
	 *         set, and once the set is entered there is no path to traverse
	 *         outside of the set.
	 * @throws IllegalArgumentException if any of the specified nodes are not in
	 * the graph.
	 * @since 2.0
	 */
	boolean isKnot(Set<N> knotedNodes);
}
