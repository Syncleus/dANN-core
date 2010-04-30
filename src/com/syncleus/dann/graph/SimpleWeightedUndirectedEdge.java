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

public class SimpleWeightedUndirectedEdge<N> extends SimpleUndirectedEdge<N> implements WeightedBidirectedEdge<N>
{
	private static final long serialVersionUID = 4622255810663472765L;
	
	private final double weight;

	public SimpleWeightedUndirectedEdge(N left, N right, double weight)
	{
		super(left, right);
		this.weight = weight;
	}

	public double getWeight()
	{
		return this.weight;
	}
}
