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

/**
 * A type of graph consisting of bidirected edges that is weakly connected,
 * acyclic, and simple.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public interface TreeGraph<N, E extends BidirectedEdge<N>> extends BidirectedGraph<N, E>
{
	/**
	 * Determines if the specified node has exactly one incomming traversable edge,
	 * and no outgoing traversable edges. In the case of undirected edges if the
	 * node hs a degree of exactly one it is a leaf. In the case of directed edges
	 * a leaf has exactly one incomming edge and no other outgoing edges. Edges
	 * whitch are not traversable in either direction are not counted. returns
	 * false if the node has no edges or has no incomming edges.
	 *
	 * @param node the node to check if it is a leaf.
	 * @return true if the specified node has exactly one incomming traversable
	 *         edge, and no outgoing traversable edges.
	 * @throws IllegalArgumentException if node does not exist in the graph.
	 * @since 2.0
	 */
	boolean isLeaf(N node);
	/**
	 * Determines if the specified edge is traversable and has a leaf node as its
	 * destination. The edge must exist in the graph.
	 *
	 * @param edge the edge to check if it is a leaf.
	 * @return true if the specified edge is traversable and has a leaf node as its
	 *         destination.
	 * @throws IllegalArgumentException if edge does not exist in the graph.
	 * @see TreeGraph#isLeaf(java.lang.Object)
	 * @since 2.0
	 */
	boolean isLeaf(E edge);
}
