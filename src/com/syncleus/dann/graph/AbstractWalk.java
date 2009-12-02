/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph;

public abstract class AbstractWalk<N, E extends Edge<? extends N>> implements Walk<N,E>
{
	public boolean isClosed()
	{
		if( this.getFirstNode().equals(this.getLastNode()))
			return true;
		return false;
	}

	public int getLength()
	{
		return this.getSteps().size();
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

	public boolean isIndependent(Walk walk)
	{
		return false;
	}
}
