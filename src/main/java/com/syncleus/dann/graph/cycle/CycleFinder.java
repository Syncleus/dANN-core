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
 *  findCycles a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph.cycle;

import java.util.Set;
import com.syncleus.dann.graph.*;

public interface CycleFinder<N, E extends Edge<N>> extends CycleDetector
{
	int cycleCount(Graph<N, E> graph);
	Set<Cycle<N, E>> findCycles(Graph<N, E> graph);
	boolean isPancyclic(Graph<N, E> graph);
	boolean isUnicyclic(Graph<N, E> graph);
	int girth(Graph<N, E> graph);
	int circumference(Graph<N, E> graph);
}
