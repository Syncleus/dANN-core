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

import java.util.ArrayList;
import java.util.List;

public class SimplePath<N, E extends TraversableCloud<N>> extends SimpleWalk<N, E> implements Path<N, E>
{
	private static final double DEFAULT_WEIGHT = 0.0;

	private final N firstNode;
	private final N lastNode;

	public SimplePath(final N ourFirstNode, final N ourLastNode, final List<E> ourSteps, final List<N> ourNodeSteps, final double defaultWeight)
	{
		super(ourSteps, ourNodeSteps, defaultWeight);

		if( ourFirstNode == null )
			throw new IllegalArgumentException("ourFirstNode can not be null");
		if( ourLastNode == null )
			throw new IllegalArgumentException("ourLastNode can not be null");
		if( !ourSteps.get(0).getEndpoints().contains(ourFirstNode) )
			throw new IllegalArgumentException("ourFirstNode is not a end point in the first ourNodeSteps");
		if( !ourSteps.get(ourSteps.size() - 1).getEndpoints().contains(ourLastNode) )
			throw new IllegalArgumentException("ourLastNode is not a end point in the last ourNodeSteps");

		this.firstNode = ourFirstNode;
		this.lastNode = ourLastNode;
	}

	public SimplePath(final N ourFirstNode, final N ourLastNode, final List<E> steps, final List<N> ourNodeSteps)
	{
		this(ourFirstNode, ourLastNode, steps, ourNodeSteps, DEFAULT_WEIGHT);
	}

	private static <N, E extends TraversableCloud<N>> List<N> edgeToNodeSteps(final N firstNode, final List<E> ourSteps)
	{
		if( firstNode == null )
			throw new IllegalArgumentException("firstNode can not be null");
		if( ourSteps == null )
			throw new IllegalArgumentException("ourSteps can not be null");
		if( ourSteps.contains(null) )
			throw new IllegalArgumentException("ourSteps can not contain a null");
		if( ourSteps.size() < 1 )
			throw new IllegalArgumentException("ourSteps can not be empty");

		final List<N> newNodeSteps = new ArrayList<N>();
		N nextNodeStep = firstNode;
		for(final E edgeStep : ourSteps)
		{
			if( !(edgeStep instanceof BidirectedEdge) )
				throw new IllegalArgumentException("this constructor can only be called when all ourSteps are BidirectedEdge");

			newNodeSteps.add(nextNodeStep);

			final List<N> nextNodes = new ArrayList<N>(edgeStep.getEndpoints());
			nextNodes.remove(nextNodeStep);
			nextNodeStep = nextNodes.get(0);
		}
		newNodeSteps.add(nextNodeStep);

		return newNodeSteps;
	}

	public SimplePath(final N ourFirstNode, final N ourLastNode, final List<E> ourSteps, final double defaultWeight)
	{
		this(ourFirstNode, ourLastNode, ourSteps, SimplePath.<N, E>edgeToNodeSteps(ourFirstNode, ourSteps), defaultWeight);
	}

	public SimplePath(final N ourFirstNode, final N ourLastNode, final List<E> steps)
	{
		this(ourFirstNode, ourLastNode, steps, DEFAULT_WEIGHT);
	}

	@Override
	protected boolean verify(final List<N> potentialNodeSteps, final List<E> potentialEdgeSteps)
	{
		return (super.verify(potentialNodeSteps, potentialEdgeSteps)) && (com.syncleus.dann.graph.AbstractPath.verifyUtility(potentialNodeSteps, potentialEdgeSteps));
	}

	@Override
	public boolean isIndependent(final Path<N, E> path)
	{
		return AbstractPath.isIndependentUtility(this, path);
	}

	@Override
	public boolean isCycle()
	{
		return false;
	}

	@Override
	public boolean isChain()
	{
		return AbstractPath.isChain(this);
	}

	@Override
	public N getFirstNode()
	{
		return this.firstNode;
	}

	@Override
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
		if( object == null )
			return false;

		if( !(object instanceof Path) )
			return false;

		return AbstractPath.equalsUtility(this, object);
	}
}
