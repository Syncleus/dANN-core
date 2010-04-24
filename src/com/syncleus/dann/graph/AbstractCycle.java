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

public abstract class AbstractCycle<N, E extends Edge<N>> extends AbstractWalk<N,E> implements Cycle<N,E>
{
	@Override
	protected boolean verify(List<N> nodeSteps, List<E> edgeSteps)
	{
		if( (super.verify(nodeSteps, edgeSteps)) && (AbstractCycle.verifyUtility(nodeSteps, edgeSteps)) )
			return true;
		return false;
	}

	static <N, E extends Edge<? extends N>> boolean verifyUtility(List<N> nodeSteps, List<E> edgeSteps)
	{
		if(nodeSteps.size()<2)
			throw new IllegalArgumentException("Wrong number of nodes or steps");
		if(!nodeSteps.get(0).equals(nodeSteps.get(nodeSteps.size()-1)))
			return false;

		return true;
	}

	public boolean isOddCycle()
	{
		return isOddCycle(this);
	}

	static boolean isOddCycle(Cycle cycle)
	{
		return (cycle.getSteps().size()%2 == 1);
	}

	@Override
	public boolean isCycle()
	{
		return true;
	}
}
