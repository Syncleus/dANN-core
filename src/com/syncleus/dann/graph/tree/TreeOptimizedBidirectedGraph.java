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
package com.syncleus.dann.graph.tree;

import com.syncleus.dann.graph.BidirectedEdge;
import com.syncleus.dann.graph.BidirectedGraph;

public interface TreeOptimizedBidirectedGraph<N, E extends BidirectedEdge<N>> extends BidirectedGraph<N, E>
{
	/**
	 * Determines if the graph is a simple acyclic graph with no more than one
	 * undirected edge between any two nodes, there can be muliple directed edges
	 * so long as the graph remains simple and acyclic. if there are no edges or
	 * nodes this returns true.
	 *
	 * @return true if the graph is a polytree, false otherwise.
	 * @since 2.0
	 */
	boolean isPolytree();
}
