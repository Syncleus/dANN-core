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

public class SimpleWalk<N, E extends Edge<N>> extends AbstractWalk<N, E>
{
	private static final double DEFAULT_WEIGHT = 0.0;

	private final List<E> steps;
	private final List<N> nodeSteps;
	private final double totalWeight;

	public SimpleWalk(final List<E> ourSteps, final List<N> ourNodeSteps, final double defaultWeight)
	{
		if( !this.verify(ourNodeSteps, ourSteps) )
			throw new IllegalArgumentException("Steps and ourNodeSteps is not consistent with a walk");

		this.steps = Collections.unmodifiableList(new ArrayList<E>(ourSteps));
		this.nodeSteps = Collections.unmodifiableList(new ArrayList<N>(ourNodeSteps));
		this.totalWeight = calculateWeight(defaultWeight);
	}

	public SimpleWalk(final List<E> ourSteps, final List<N> ourNodeSteps)
	{
		this(ourSteps, ourNodeSteps, DEFAULT_WEIGHT);
	}

	@Override
	public final List<E> getSteps()
	{
		return this.steps;
	}

	@Override
	public final List<N> getNodeSteps()
	{
		return this.nodeSteps;
	}

	@Override
	public final double getWeight()
	{
		return this.totalWeight;
	}
}
