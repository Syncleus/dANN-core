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
 * A type of graph where each edge has 2 or more end points and is undirected
 * so each edge can be traversed from any end point of the edge to any end
 * point.
 *
 * @author Jeffrey Phillips Freeman
 * @param <N> The class representing the nodes of the class
 * @param <E> The HyperEdge representing the edges of the graph.
 * @since 2.0
 */
public interface HyperGraph<N, E extends HyperEdge<N>> extends Graph<N,E>
{
	/**
	 * Determines the number of end points of the edge with the largest number
	 * of end points. The graph must have at least one edge.
	 *
	 * @throws IllegalStateException thrown if no edges exist.
	 * @return the number of end points of the edge with the largest number
	 * of end points. The graph must have at least one edge.
	 * @since 2.0
	 */
	int getRank();

	/**
	 * Determines if all edges have exactly the same number of end points, if
	 * so this returns true, otherwise returns false.
	 *
	 * @throws IllegalStateException thrown if no edges exist.
	 * @return true if all edges have exactly the same number of end points,
	 * otherwise returns false.
	 * @since 2.0
	 */
	boolean isUniform();

	/**
	 * Determines if the specifed graph has all the nodes in this graph and
	 * some but not all of the edges.
	 *
	 * @param partialGraph the graph to check if it is a partial hypergraph.
	 * @return true if the specifed graph has all the nodes in this graph and
	 * some but not all of the edges.
	 * @since 2.0
	 */
	boolean isPartial(HyperGraph<N,E> partialGraph);

	/**
	 * Determines if the specified graph has all the nodes in this graph and
	 * if for every edge in this graph there is an induced connected subgraph
	 * in the specified graph.
	 *
	 * @param hostGraph the graph to check if it is a host graph.
	 * @return true if the specified graph is a host graph of this one.
	 * @since 2.0
	 */
	boolean isHost(HyperGraph<N,E> hostGraph);

	/**
	 * Creates a undirected graph that contains all the nodes of this graph and
	 * each pair of nodes in every hyperedge has a undirected edge in the new
	 * graph.
	 *
	 * @return the primal graph of this graph.
	 * @since 2.0
	 */
	BidirectedGraph<N,BidirectedEdge<N>> getPrimal();
}
