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

import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;

public interface TreeOptimizedGraph<N, E extends Edge<N>> extends Graph<N, E>
{
	/**
	 * Determines if the specified graph is a spanning tree of this graph. This
	 * will return true if the specified graph contains all the nodes from this
	 * graph, is weakly connected, and is acyclic, otherwise it returns false.
	 *
	 * @param subGraph graph to check if it is a spanning tree of this.
	 * @return true if the specified graph contains all the nodes from this graph,
	 *         is weakly connected, and is acyclic, otherwise it returns false.
	 * @since 2.0
	 */
	boolean isSpanningTree(Graph<N, E> subGraph);
	/**
	 * Determines if this graph is weakly connected, acyclic, and simple, another
	 * words a Tree. Some rooted tree's are directed trees, however not all
	 * directed tree's are rooted.
	 *
	 * @return true if this graph is weakly connected, acyclic, and simple, another
	 *         words a Tree.
	 * @since 2.0
	 */
	boolean isTree();
	/**
	 * Determined if this graph contains maximally connected components which are
	 * all Trees. If this grap is itself a tree this returns true.
	 *
	 * @return true if this graph contains maximally connected components which are
	 *         all Trees.
	 * @since 2.0
	 */
	boolean isForest();
}
