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
 * A type of graph where each edge has 2 or more end points and is undirected so
 * each edge can be traversed from any end point of the edge to any end point.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public interface HyperGraph<N, E extends HyperEdge<N>> extends Graph<N, E>
{
	/**
	 * True if this graph enforces its edges to have a maximum rank.
	 *
	 * @since 2.0
	 * @return true if there is a maximum allowable rank for this graph, false otherwise.
	 */
	boolean hasMaximumAllowableRank();

	/**
	 * Returns the maximum allowable rank for each edge, -1 if there is no limit.
	 *
	 * @return the maximum allowable rank for each edge, -1 if there is no limit.
	 * @since 2.0
	 */
	int getMaximumAllowableRank();

	/**
	 * True if this graph enforces its edges to have a minimum rank.
	 *
	 * @return true if there is a minimum allowable rank for this graph, false otherwise.
	 * @since 2.0
	 */
	boolean hasMinimumAllowableRank();

	/**
	 * Returns the minimum allowable rank for each edge, -1 if there is no limit.
	 *
	 * @return the maximum allowable rank for each edge, -1 if there is no limit.
	 * @since 2.0
	 */
	int getMinimumAllowableRank();
}
