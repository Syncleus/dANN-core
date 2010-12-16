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

import java.util.List;
// TODO refine the verify and equals method to match a cycles definition of
// unique. i.e. no repeat in nodes or edges, sequence matters but starting point doesnt.

public abstract class AbstractCycle<N, E extends Edge<N>> extends AbstractWalk<N, E> implements Cycle<N, E>
{
	@Override
	protected boolean verify(final List<N> nodeSteps, final List<E> edgeSteps)
	{
		return (super.verify(nodeSteps, edgeSteps)) && (com.syncleus.dann.graph.AbstractCycle.verifyUtility(nodeSteps, edgeSteps));
	}

	static <N, E extends Edge<? extends N>> boolean verifyUtility(final List<N> nodeSteps, final List<E> edgeSteps)
	{
		if( nodeSteps.size() < 2 )
			throw new IllegalArgumentException("Wrong number of nodes or steps");
		return nodeSteps.get(0).equals(nodeSteps.get(nodeSteps.size() - 1));
	}

	public boolean isOddCycle()
	{
		return isOddCycle(this);
	}

	static boolean isOddCycle(final Cycle cycle)
	{
		return (cycle.getSteps().size() % 2 != 0);
	}

	@Override
	public boolean isCycle()
	{
		return true;
	}
}
