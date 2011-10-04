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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractWalk<N, E extends Cloud<N>> extends AbstractPath<N, E> implements Walk<N, E>
{
	@Override
	protected boolean verify(final List<N> nodeSteps, final List<E> edgeSteps)
	{
		return ((super.verify(nodeSteps, edgeSteps)) && (verifyUtility(nodeSteps, edgeSteps)));
	}

	static <N, E extends Cloud<N>> boolean verifyUtility(final List<N> nodeSteps, final List<E> edgeSteps)
	{
		if( nodeSteps.size() < 2 )
			throw new IllegalArgumentException("Wrong number of nodes or steps");
		return !(nodeSteps.get(0).equals(nodeSteps.get(nodeSteps.size() - 1)));
	}

	@Override
	public boolean isChain()
	{
		return isChain(this);
	}

	protected static <N, E extends Cloud<N>> boolean isChain(final Walk<N, E> walk)
	{
		final Set<N> uniqueNodes = new HashSet<N>(walk.getNodeSteps());
		final Set<E> uniqueEdges = new HashSet<E>(walk.getSteps());
		if( uniqueNodes.size() < walk.getNodeSteps().size() )
			return false;
		return !(uniqueEdges.size() < walk.getSteps().size());
	}

	@Override
	public boolean isIndependent(final Walk<N, E> walk)
	{
		return AbstractWalk.isIndependentUtility(this, walk);
	}

	static <N, E extends Cloud<N>> boolean isIndependentUtility(final Walk<N, E> firstWalk, final Walk<N, E> secondWalk)
	{
		if( !firstWalk.getFirstNode().equals(secondWalk.getFirstNode()) )
			return false;
		if( !firstWalk.getLastNode().equals(secondWalk.getLastNode()) )
			return false;
		final List<N> exclusiveFirstNodes = new ArrayList<N>(firstWalk.getNodeSteps());
		exclusiveFirstNodes.remove(exclusiveFirstNodes.size() - 1);
		exclusiveFirstNodes.remove(0);
		final List<N> secondNodes = new ArrayList<N>(secondWalk.getNodeSteps());
		secondNodes.remove(secondNodes.size() - 1);
		secondNodes.remove(0);
		exclusiveFirstNodes.removeAll(secondNodes);
		return !(exclusiveFirstNodes.size() < firstWalk.getNodeSteps().size());
	}

	@Override
	public boolean isCycle()
	{
		return false;
	}

	static int hashCodeUtility(final Walk walk)
	{
		return (walk.getNodeSteps().hashCode() + walk.getSteps().hashCode()) * walk.getSteps().hashCode();
	}

	static boolean equalsUtility(final Walk walk, final Object object)
	{
		if( (walk == null) || (object == null) )
			return false;
		final Walk secondWalk = (Walk) object;
		if( !(secondWalk.getNodeSteps().equals(walk.getNodeSteps())) )
			return false;
		return secondWalk.getSteps().equals(walk.getSteps());
	}

	@Override
	public int hashCode()
	{
		return AbstractWalk.hashCodeUtility(this);
	}

	@Override
	public boolean equals(final Object object)
	{
		if( object == null )
			return false;

		if( !(object instanceof Walk) )
			return false;

		return AbstractWalk.equalsUtility(this, object);
	}
}
