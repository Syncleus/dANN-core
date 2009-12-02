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
import java.util.Collections;
import java.util.List;

public class SimpleHyperWalk<N, E extends HyperEdge<? extends N>> extends AbstractWalk<N,E> implements HyperWalk<N,E>
{
	private final List<E> steps;
	private final List<N> nodeSteps;
	private final N firstNode;
	private final N lastNode;

	public SimpleHyperWalk(N firstNode, N lastNode, List<E> steps, List<N> nodeSteps)
	{
		this.steps = Collections.unmodifiableList(new ArrayList<E>(steps));
		this.nodeSteps = Collections.unmodifiableList(new ArrayList<N>(nodeSteps));
		this.firstNode = firstNode;
		this.lastNode = lastNode;
	}

	public List<E> getSteps()
	{
		return this.steps;
	}

	public List<N> getNodeSteps()
	{
		return this.nodeSteps;
	}

	public N getFirstNode()
	{
		return this.firstNode;
	}

	public N getLastNode()
	{
		return this.lastNode;
	}
}
