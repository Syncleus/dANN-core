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

public class SimplePath<N, E extends Edge<N>> extends SimpleWalk<N,E> implements Path<N,E>
{
	private final N firstNode;
	private final N lastNode;
	
	public SimplePath(final N firstNode, final N lastNode, final List<E> steps, final List<N> nodeSteps, final double defaultWeight)
	{
		super(steps, nodeSteps, defaultWeight);

		if(firstNode == null)
			throw new IllegalArgumentException("firstNode can not be null");
		if(lastNode == null)
			throw new IllegalArgumentException("lastNode can not be null");
		if(!steps.get(0).getNodes().contains(firstNode))
			throw new IllegalArgumentException("firstNode is not a end point in the first nodeSteps");
		if(!steps.get(steps.size()-1).getNodes().contains(lastNode))
			throw new IllegalArgumentException("lastNode is not a end point in the last nodeSteps");

		this.firstNode = firstNode;
		this.lastNode = lastNode;
	}

	public SimplePath(final N firstNode, final N lastNode, final List<E> steps, final List<N> nodeSteps)
	{
		this(firstNode, lastNode, steps, nodeSteps, 0.0);
	}

	private static <N, E extends Edge<N>> List<N> edgeToNodeSteps(final N firstNode, final List<E> steps)
	{
		if(firstNode == null)
			throw new IllegalArgumentException("firstNode can not be null");
		if(steps == null)
			throw new IllegalArgumentException("steps can not be null");
		if(steps.contains(null))
			throw new IllegalArgumentException("steps can not contain a null");
		if(steps.size() < 1)
			throw new IllegalArgumentException("steps can not be empty");

		final List<N> newNodeSteps = new ArrayList<N>();
		N nextNodeStep = firstNode;
		for(final E edgeStep : steps)
		{
			if(!(edgeStep instanceof BidirectedEdge))
				throw new IllegalArgumentException("this constructor can only be called when all steps are BidirectedEdge");

			newNodeSteps.add(nextNodeStep);

			final List<N> nextNodes = new ArrayList<N>(edgeStep.getNodes());
			nextNodes.remove(nextNodeStep);
			nextNodeStep = nextNodes.get(0);
		}
		newNodeSteps.add(nextNodeStep);

		return newNodeSteps;
	}

	public SimplePath(final N firstNode, final N lastNode, final List<E> steps, final double defaultWeight)
	{
		this(firstNode, lastNode, steps, SimplePath.<N,E>edgeToNodeSteps(firstNode, steps), defaultWeight);
	}

	public SimplePath(final N firstNode, final N lastNode, final List<E> steps)
	{
		this(firstNode, lastNode, steps, 0.0);
	}

	@Override
	protected boolean verify(final List<N> nodeSteps, final List<E> edgeSteps)
	{
		if( (super.verify(nodeSteps, edgeSteps)) && (AbstractPath.verifyUtility(nodeSteps, edgeSteps)) )
			return true;
		return false;
	}

	public boolean isIndependent(final Path<N,E> path)
	{
		return AbstractPath.isIndependentUtility(this, path);
	}

	@Override
	public boolean isCycle()
	{
		return false;
	}

	public boolean isChain()
	{
		return AbstractPath.isChain(this);
	}

	public N getFirstNode()
	{
		return this.firstNode;
	}

	public N getLastNode()
	{
		return this.lastNode;
	}

	@Override
	public int hashCode()
	{
		return AbstractPath.hashCodeUtility(this);
	}

	@Override
	public boolean equals(final Object object)
	{
		if(object == null)
			return false;

		if(!(object instanceof Path))
			return false;
		
		return AbstractPath.equalsUtility(this, object);
	}
}
