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

public interface StrongConnectivityOptimizedGraph<N, E extends Edge<N>> extends Graph<N, E>
{
	/**
	 * Determines if there is a path from the firstNode to the lastNode. There may
	 * be a path from firstNode to lastNode even if there is no path from lastNode
	 * to firstNode. This is because it only checks if there is a traversable path.
	 * Both nodes must be present in the graph or else an InvalidArgumentException
	 * will be thrown.
	 *
	 * @param firstNode begining node to find a path from.
	 * @param lastNode eding node to find a path to.
	 * @return true if a path exists, false otherwise.
	 * @since 2.0
	 */
	boolean isStronglyConnected(N firstNode, N lastNode);
	/**
	 * If there is atleast one path from every node in the graph to any other node
	 * in the graph then true, false otherwise. There must be a traversable path,
	 * not just a series of adjacency.
	 *
	 * @return true if the graph is connected, false otherwise.
	 * @since 2.0
	 */
	boolean isStronglyConnected();
}
