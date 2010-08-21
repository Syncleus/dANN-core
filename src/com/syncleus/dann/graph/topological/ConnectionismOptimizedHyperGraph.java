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

import com.syncleus.dann.graph.HyperEdge;
import com.syncleus.dann.graph.HyperGraph;

public interface ConnectionismOptimizedHyperGraph<N, E extends HyperEdge<N>> extends HyperGraph<N, E>
{
	/**
	 * Determines the number of end points of the edge with the largest number of
	 * end points. The graph must have at least one edge.
	 *
	 * @return the number of end points of the edge with the largest number of end
	 *         points. The graph must have at least one edge.
	 * @throws IllegalStateException thrown if no edges exist.
	 * @since 2.0
	 */
	int getRank();
}
