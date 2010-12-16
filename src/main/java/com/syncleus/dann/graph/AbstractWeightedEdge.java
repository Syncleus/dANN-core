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

public abstract class AbstractWeightedEdge<N> extends AbstractEdge<N> implements WeightedEdge<N>
{
	private final double weight;

    protected AbstractWeightedEdge(final double weight)
    {
		super();
		this.weight = weight;
    }

    protected AbstractWeightedEdge(final double weight, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
    {
		super(allowJoiningMultipleGraphs, contextEnabled);
		this.weight = weight;
	}

	protected AbstractWeightedEdge(final List<N> nodes, final double ourWEight)
	{
		super(nodes);
		this.weight = ourWEight;
	}

	protected AbstractWeightedEdge(final double ourWeight, final N... nodes)
	{
		super(nodes);
		this.weight = ourWeight;
	}

	@Override
	public double getWeight()
	{
		return this.weight;
	}

	@Override
	public AbstractWeightedEdge<N> clone()
	{
		return (AbstractWeightedEdge<N>) super.clone();
	}
}
