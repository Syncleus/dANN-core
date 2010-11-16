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
package com.syncleus.dann.graph.cycle;

import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;

public interface CycleOptimizedGraph<N, E extends Edge<N>> extends Graph<N, E>
{
	/**
	 * Calculates the number of cylces in the graph. A cycle is any walk that
	 * starts and ends on the same node and does not repeat any nodes or edges. Two
	 * cycles are considered the same if they contain the sequence of nodes and
	 * edges even if their starting point is different. In fact cycles do not have
	 * a non-arbitrary starting and ending point. If this returns 0 then isAcyclic
	 * will be true. If this returns 1 then isUnicyclic will be true.
	 *
	 * @return The number of cycles in the graph, 0 if the graph is acyclic, 1 if
	 *         unicyclic.
	 * @since 2.0
	 */
	int getCycleCount();
	/**
	 * Determines if there is a cycle of every possible length in the graph, not
	 * including lengths less than 3, and that the graph is simple. Therefore the
	 * graph can not have any loops. Returns true if the order of the graph is less
	 * than 3. Returns false if the graph is not simple.
	 *
	 * @return true there is a cycle of every possible length in the graph, not
	 *         including lengths less than 3, and that the graph is simple.
	 * @since 2.0
	 */
	boolean isPancyclic();
	/**
	 * Determines if this is a simple graph with exactly one cycle. Since the graph
	 * must be simple it can not have any loops.
	 *
	 * @return true there is exactly one cycle and the graph is simple, false
	 *         otherwise.
	 * @since 2.0
	 */
	boolean isUnicyclic();
	/**
	 * Determines if the graph contains no cycles or loops. Since the graph need
	 * not be a simple graph the graph can have multiple edges between nodes
	 * however there can not be loops if it is to qualify as acyclic.
	 *
	 * @return true if there are no cycles or loops, false otherwise.
	 * @since 2.0
	 */
	boolean isAcyclic();
	/**
	 * Finds the length of the shortest cycle in the graph, -1 if the graph is
	 * acyclic. If the graph contains any loops this should return 0.
	 *
	 * @return The length of the shortest cycle in the graph, -1 if the graph is
	 *         acyclic.
	 * @since 2.0
	 */
	int getGirth();
	/**
	 * Finds the length of the longest cycle in the graph, -1 if the graph is
	 * acyclic.
	 *
	 * @return The length of the longest cycle in the graph, -1 if the graph is
	 *         acyclic.
	 * @since 2.0
	 */
	int getCircumference();
}
