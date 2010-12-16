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

public final class ImmutableWeightedHyperEdge<N> extends AbstractHyperEdge<N> implements WeightedEdge<N>
{
	private static final long serialVersionUID = 2622882478754498808L;
	private final double weight;

	public ImmutableWeightedHyperEdge(final List<N> nodes, final double ourWeight)
	{
		super(nodes);
		this.weight = ourWeight;
	}

	public ImmutableWeightedHyperEdge(final List<N> nodes, final double ourWeight, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(nodes, allowJoiningMultipleGraphs, contextEnabled);
		this.weight = ourWeight;
	}

	@Override
	public double getWeight()
	{
		return this.weight;
	}

	@Override
	public ImmutableWeightedHyperEdge<N> disconnect(final N node)
	{
		return (ImmutableWeightedHyperEdge<N>) super.remove(node);
	}

	@Override
	public ImmutableWeightedHyperEdge<N> disconnect(final List<N> nodes)
	{
		return (ImmutableWeightedHyperEdge<N>) super.remove(nodes);
	}

	@Override
	public ImmutableWeightedHyperEdge<N> clone()
	{
		return (ImmutableWeightedHyperEdge<N>) super.clone();
	}
}
