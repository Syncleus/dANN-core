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

import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;

public interface StructureOptimizedGraph<N, E extends Edge<N>> extends Graph<N, E>
{
	/**
	 * Determines if the specified graph only contains nodes and edges that are
	 * also present in this graph. If graph is empty then this will always return
	 * true.
	 *
	 * @return true if the specified graph only contains nodes and edges that are
	 *         also present in this graph.
	 * @since 2.0
	 */
	boolean isSubGraph(Graph<N, E> graph);
	/**
	 * Determines if all the nodes in the specified graph contains all the same
	 * nodes as this graph and every pair of nodes that are adjacent in this graph
	 * are adjacent in the specified graph, and vice versa. For bidirected edges
	 * this means both graphs have equal nodes and the same number of edges each
	 * eith the same end points however the traversability of the edges may differ
	 * between graphs. In Hypergraphs there can be an entirely different number of
	 * edges and still be isomorphic. If there are no edges and/or there are no
	 * nodes in both graphs then they are isomorphic.
	 *
	 * @param isomorphicGraph graph to check if it is isomorphic with this graph.
	 * @return true if this graph and the specified graph have all the same nodes
	 *         and all the same pairs of adjacecy betwee them. False otherwise.
	 * @since 2.0
	 */
	boolean isIsomorphic(Graph<N, E> isomorphicGraph);
	/**
	 * Determines if all the nodes in the specified graph contains all the same
	 * nodes as this graph and every pair of nodes that are adjacent in this graph
	 * are adjacent in the specified graph. Unlike isIsomorphic this is a one way
	 * comparison, so while every adjacent pair of nodes in this graph must be
	 * present in the specified graph, there may be adjacenct nodes in the
	 * specified graph that are not adjacent in this graph. That also means the
	 * specified graph may have nodes that dont exist in this graph, but every node
	 * in this graph must exist in the specified graph. If there are no edges
	 * and/or there are no nodes in both graphs then they are homomorphic.
	 *
	 * @param homomorphicGraph graph to check if it is homomorphic with this
	 * graph.
	 * @return true if every pair of adjacent nodes in this graph are also present
	 *         and adjacent in the specified graph.
	 * @since 2.0
	 */
	boolean isHomomorphic(Graph<N, E> homomorphicGraph);
}
