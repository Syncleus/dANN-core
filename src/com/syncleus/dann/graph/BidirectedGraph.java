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

import java.util.*;

public interface BidirectedGraph<N, E extends BidirectedEdge<N>> extends Graph<N,E>
{
	Set<E> getOutEdges(N node);
	Set<E> getInEdges(N node);
	int getIndegree(N node);
	int getOutdegree(N node);

	/**
	 * If there is atleast one path from every Node in the graph to any other
	 * node in the graph, treating all edges as undirected edges, then true,
	 * false otherwise.
	 *
	 * @see Graph#isConnected
	 * @return true if the graph is connected, false otherwise.
	 */
	boolean isWeaklyConnected();
	boolean isPolytree();
	boolean isKnot();
}
