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

public class SimpleCycle<N, E extends Edge<N>> extends SimpleWalk<N,E> implements Cycle<N,E>
{
	public SimpleCycle(final List<E> steps, final List<N> nodes, final double defaultWeight)
	{
		super(steps, nodes, defaultWeight);
	}

	public SimpleCycle(final List<E> steps, final double defaultWeight)
	{
		this(steps, SimpleCycle.<N,E>edgeToNodeSteps(steps), defaultWeight);
	}

	public SimpleCycle(final List<E> steps, final List<N> nodes)
	{
		super(steps, nodes, 0.0);
	}

	public SimpleCycle(final List<E> steps)
	{
		this(steps, SimpleCycle.<N,E>edgeToNodeSteps(steps), 0.0);
	}
	
	private static <N, E extends Edge<N>> N startNodeFromSteps(final List<E> steps)
	{
		if(steps.size() == 1)
			return steps.get(0).getNodes().get(0);

		final List<N> exclusiveFirstNodes = new ArrayList<N>(steps.get(0).getNodes());
		exclusiveFirstNodes.removeAll(steps.get(1).getNodes());
		if(exclusiveFirstNodes.size() == 1)
			return exclusiveFirstNodes.get(0);
		else if(exclusiveFirstNodes.isEmpty())
			return steps.get(0).getNodes().get(0);
		else
			throw new IllegalArgumentException("steps does not form a path");
	}

	@Override
	protected boolean verify(final List<N> nodeSteps, final List<E> edgeSteps)
	{
		if( (super.verify(nodeSteps, edgeSteps)) && (AbstractCycle.verifyUtility(nodeSteps, edgeSteps)) )
			return true;
		return false;
	}

	private static <N, E extends Edge<N>> List<N> edgeToNodeSteps(final List<E> steps)
	{
		if(steps == null)
			throw new IllegalArgumentException("steps can not be null");
		if(steps.contains(null))
			throw new IllegalArgumentException("steps can not contain a null");
		if(steps.size() < 1)
			throw new IllegalArgumentException("steps can not be empty");

		final List<N> newNodeSteps = new ArrayList<N>();
		N nextNodeStep = SimpleCycle.<N,E>startNodeFromSteps(steps);
		for(E edgeStep : steps)
		{
			if(!(edgeStep instanceof BidirectedEdge))
				throw new IllegalArgumentException("all steps are not BidirectedEdge");

			newNodeSteps.add(nextNodeStep);

			final List<N> nextNodes = new ArrayList<N>(edgeStep.getNodes());
			nextNodes.remove(nextNodeStep);
			nextNodeStep = nextNodes.get(0);
		}
		newNodeSteps.add(nextNodeStep);

		return newNodeSteps;
	}

	public boolean isOddCycle()
	{
		return AbstractCycle.isOddCycle(this);
	}

	@Override
	protected double calculateWeight(final double defaultWeight)
	{
		final N startNode = this.getNodeSteps().get(0);
		double endNodeWeight = 0.0;
		if(startNode instanceof Weighted)
			endNodeWeight = ((Weighted)startNode).getWeight();
		return super.calculateWeight(defaultWeight) - endNodeWeight;
	}

	@Override
	public boolean isCycle()
	{
		return true;
	}
}
