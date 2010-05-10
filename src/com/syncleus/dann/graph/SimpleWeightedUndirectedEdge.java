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

public class SimpleWeightedUndirectedEdge<N> extends ImmutableUndirectedEdge<N> implements WeightedBidirectedEdge<N>, MutableWeighted
{
	private static final long serialVersionUID = 4622255810663472765L;
	private double weight;

	public SimpleWeightedUndirectedEdge(final N left, final N right, final double weight)
	{
		super(left, right);
		this.weight = weight;
	}

	public double getWeight()
	{
		return this.weight;
	}

	public void setWeight(final double newWeight)
	{
		this.weight = newWeight;
	}

	@Override
	public SimpleWeightedUndirectedEdge<N> disconnect(final N node)
	{
		if (node == null)
			throw new IllegalArgumentException("node can not be null");
		if (!this.getNodes().contains(node))
			throw new IllegalArgumentException("node is not currently connected to");
		return (SimpleWeightedUndirectedEdge<N>) this.remove(node);
	}

	@Override
	public SimpleWeightedUndirectedEdge<N> disconnect(final List<N> nodes)
	{
		if (nodes == null)
			throw new IllegalArgumentException("node can not be null");
		if (!this.getNodes().containsAll(nodes))
			throw new IllegalArgumentException("node is not currently connected to");
		return (SimpleWeightedUndirectedEdge<N>) this.remove(nodes);
	}

	@Override
	public SimpleWeightedUndirectedEdge<N> clone()
	{
		return (SimpleWeightedUndirectedEdge<N>) super.clone();
	}
}
