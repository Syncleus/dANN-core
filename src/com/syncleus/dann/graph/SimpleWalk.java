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

public class SimpleWalk<N, E extends Edge<? extends N>> extends AbstractWalk<N,E>
{
	private final List<E> steps;
	private final N firstNode;
	private final N lastNode;

	public SimpleWalk(N firstNode, N lastNode, List<E> steps)
	{
		if(firstNode == null)
			throw new IllegalArgumentException("firstNode can not be null");
		if(lastNode == null)
			throw new IllegalArgumentException("lastNode can not be null");
		if(steps == null)
			throw new IllegalArgumentException("steps can not be null");
		if(steps.contains(null))
			throw new IllegalArgumentException("steps can not contain a null");
		
		this.steps = Collections.unmodifiableList(new ArrayList<E>(steps));
		this.firstNode = firstNode;
		this.lastNode = lastNode;
	}

	public List<E> getSteps()
	{
		return this.steps;
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
