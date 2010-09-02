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

public final class SimpleWeightedDirectedEdge<N> extends AbstractDirectedEdge<N> implements WeightedDirectedEdge<N>, MutableWeighted
{
	private static final long serialVersionUID = -6843921044147012645L;
	private double weight;

	public SimpleWeightedDirectedEdge(final N source, final N destination, final double ourWeight)
	{
		super(source, destination);
		this.weight = ourWeight;
	}

	public SimpleWeightedDirectedEdge(final N source, final N destination, final double ourWeight, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(source, destination, allowJoiningMultipleGraphs, contextEnabled);
		this.weight = ourWeight;
	}

	@Override
	public double getWeight()
	{
		return this.weight;
	}

	@Override
	public void setWeight(final double newWeight)
	{
		this.weight = newWeight;
	}

	@Override
	public SimpleWeightedDirectedEdge<N> disconnect(final N node)
	{
		return (SimpleWeightedDirectedEdge<N>) this.disconnect(node);
	}

	@Override
	public SimpleWeightedDirectedEdge<N> disconnect(final List<N> nodes)
	{
		return (SimpleWeightedDirectedEdge<N>) this.disconnect(nodes);
	}

	@Override
	public SimpleWeightedDirectedEdge<N> clone()
	{
		return (SimpleWeightedDirectedEdge<N>) super.clone();
	}
}
