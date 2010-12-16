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

import com.syncleus.dann.graph.*;

public interface ConnectionismOptimizedBidirectedGraph<N, E extends BidirectedEdge<N>> extends BidirectedGraph<N, E>
{
	/**
	 * Count all edges which traverse from the specified node. While the
	 * combination of out edges and in edges will usually result in all the
	 * adjacent edges for a node this is not strictly required.
	 *
	 * @param node The source node the returned edges will traverse from.
	 * @return count of all edges which traverse from the specified node.
	 * @throws IllegalArgumentException if node does not exist in the graph.
	 * @since 2.0
	 */
	int getOutdegree(N node);
	/**
	 * Count all edges which traverse to the specified node. While the combination
	 * of out edges and in edges will usually result in all the adjacent edges for
	 * a node this is not strictly required.
	 *
	 * @param node The destination node the edges counted will traverse to.
	 * @return count of all edges which traverse to the specified node.
	 * @throws IllegalArgumentException if node does not exist in the graph.
	 * @since 2.0
	 */
	int getIndegree(N node);
}
