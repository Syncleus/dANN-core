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
import com.syncleus.dann.graph.cycle.*;

public abstract class AbstractWalk<N, E extends Edge<N>> implements Walk<N, E>
{
	protected boolean verify(final List<N> nodeSteps, final List<E> edgeSteps)
	{
		if (edgeSteps == null)
			throw new IllegalArgumentException("steps can not be null");
		if (edgeSteps.contains(null))
			throw new IllegalArgumentException("steps can not contain a null");
		if (nodeSteps == null)
			throw new IllegalArgumentException("nodeSteps can not be null");
		if (nodeSteps.contains(null))
			throw new IllegalArgumentException("nodeSteps can not contain a null");
		if ((nodeSteps.size() != (edgeSteps.size() + 1)) || (nodeSteps.size() < 2) || (edgeSteps.size() < 1))
			throw new IllegalArgumentException("Wrong number of nodes or steps");
		int nextNodeIndex = 0;
		for(final E edgeStep : edgeSteps)
		{
			if (!edgeStep.getNodes().contains(nodeSteps.get(nextNodeIndex)))
				return false;
			nextNodeIndex++;
		}
		if (!edgeSteps.get(edgeSteps.size() - 1).getNodes().contains(nodeSteps.get(nextNodeIndex)))
			return false;
		return true;
	}

	public boolean isClosed()
	{
		if (this.getNodeSteps().get(0).equals(this.getNodeSteps().get(this.getNodeSteps().size() - 1)))
			return true;
		return false;
	}

	public int getLength()
	{
		return this.getSteps().size();
	}

	public boolean isTrail()
	{
		final Set<E> edgeSet = new HashSet<E>(this.getSteps());
		if (edgeSet.size() < this.getSteps().size())
			return false;
		return true;
	}

	public boolean isTour()
	{
		if ((this.isTrail()) && (this.isClosed()))
			return true;
		return false;
	}

	public boolean isCycle()
	{
		if (this.getNodeSteps().get(0).equals(this.getNodeSteps().get(this.getNodeSteps().size() - 1)))
			return true;
		return false;
	}

	public boolean hasChildCycles()
	{
		final Graph<N, E> graph = new ImmutableAdjacencyGraph<N, E>(new HashSet<N>(this.getNodeSteps()), new HashSet<E>(this.getSteps()));
		final CycleFinder<N, E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N, E>();
		if (this.isCycle())
			if (finder.cycleCount(graph) > 1)
				return true;
			else if (finder.hasCycle(graph))
				return true;
		return false;
	}

	protected double calculateWeight(final double defaultWeight)
	{
		double newTotalWeight = 0.0;
		for(final E step : this.getSteps())
		{
			if (step instanceof Weighted)
				newTotalWeight += ((Weighted) step).getWeight();
			else
				newTotalWeight += defaultWeight;
		}
		for(final N step : this.getNodeSteps())
		{
			if (step instanceof Weighted)
				newTotalWeight += ((Weighted) step).getWeight();
			else
				newTotalWeight += defaultWeight;
		}
		return newTotalWeight;
	}

	@Override
	public int hashCode()
	{
		final Set<N> uniqueNodes = new HashSet<N>(this.getNodeSteps());
		final Set<E> uniqueEdges = new HashSet<E>(this.getSteps());
		return (uniqueNodes.hashCode() + uniqueEdges.hashCode()) * uniqueEdges.hashCode();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(final Object object)
	{
		if (object == null)
			return false;
		if (object.getClass() != this.getClass())
			return false;
		final Walk walk = (Walk) object;
		final Set uniqueNodes = new HashSet<N>(this.getNodeSteps());
		final Set uniqueEdges = new HashSet<E>(this.getSteps());
		final Set otherUniqueNodes = new HashSet(walk.getNodeSteps());
		final Set otherUniqueEdges = new HashSet(walk.getSteps());
		if (!(uniqueNodes.equals(otherUniqueNodes)))
			return false;
		if (!(uniqueEdges.equals(otherUniqueEdges)))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return this.getSteps().toString();
	}
}
