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

public abstract class AbstractWalk<N extends Node<? extends E>, E extends Edge<? extends Node>> implements Walk<N, E>
{
	private N firstNode;
	private N lastNode;
	private List<E> steps;

	protected AbstractWalk(N firstNode, N lastNode, List<E> steps)
	{
		if(firstNode == null)
			throw new IllegalArgumentException("firstNode can not be null.");
		if(lastNode == null)
			throw new IllegalArgumentException("lastNode can not be null.");
		if(steps == null)
			throw new IllegalArgumentException("steps can not be null.");
		if(steps.size() <= 0)
			throw new IllegalArgumentException("steps must have atleast one member.");
		if(!steps.get(0).getNodes().contains(firstNode))
			throw new IllegalArgumentException("the first element in steps must contain firstNode.");
		if(!steps.get(steps.size()-1).getNodes().contains(lastNode))
			throw new IllegalArgumentException("the last element in steps must contain lastNode.");
		
		this.firstNode = firstNode;
		this.lastNode = lastNode;
		this.steps = new ArrayList<E>(steps);
	}

	public List<E> getSteps()
	{
		return Collections.unmodifiableList(this.steps);
	}

	public N getFirstNode()
	{
		return this.firstNode;
	}

	public N getLastNode()
	{
		return this.lastNode;
	}

	public boolean isClosed()
	{
		if( this.firstNode.equals(this.lastNode))
			return true;
		return false;
	}

	public int getLength()
	{
		return this.steps.size();
	}

	public boolean isTrail()
	{
		return false;
	}

	public boolean isTour()
	{
		return false;
	}

	public boolean isPath()
	{
		return false;
	}

	public boolean isCycle()
	{
		return false;
	}

	public boolean isIndependent(Walk<? extends N, ? extends E> walk)
	{
		return false;
	}
}
