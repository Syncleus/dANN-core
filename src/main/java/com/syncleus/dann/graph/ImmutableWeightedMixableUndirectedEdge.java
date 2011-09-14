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

public final class ImmutableWeightedMixableUndirectedEdge<N, LN extends N, RN extends N> extends AbstractUndirectedEdge<N, LN,RN> implements WeightedMixableBidirectedEdge<LN,RN,N>
{
	private static final long serialVersionUID = -298347598212765L;
	private final double weight;

	public ImmutableWeightedMixableUndirectedEdge(final LN left, final RN right, final double ourWeight)
	{
		super(left, right);
		this.weight = ourWeight;
	}

	public ImmutableWeightedMixableUndirectedEdge(final LN left, final RN right, final double ourWeight, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(left, right, allowJoiningMultipleGraphs, contextEnabled);
		this.weight = ourWeight;
	}

	@Override
	public double getWeight()
	{
		return this.weight;
	}

	@Override
	protected ImmutableWeightedMixableUndirectedEdge<N, LN,RN> clone()
	{
		return (ImmutableWeightedMixableUndirectedEdge<N, LN,RN>) super.clone();
	}
}
