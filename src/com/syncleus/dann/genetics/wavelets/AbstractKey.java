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
package com.syncleus.dann.genetics.wavelets;


import java.util.*;
import org.apache.log4j.Logger;
import com.syncleus.dann.genetics.MutableInteger;

public abstract class AbstractKey implements Cloneable
{
	private HashMap<Integer, Boolean> points;
	private final static Random RANDOM = Mutation.getRandom();
	private final static Logger LOGGER = Logger.getLogger(AbstractKey.class);

	protected AbstractKey()
	{
		this.points = new HashMap<Integer, Boolean>();
		this.points.put(Integer.valueOf(RANDOM.nextInt()), RANDOM.nextBoolean());
	}

	protected AbstractKey(Map<Integer, Boolean> points)
	{
		this.points = new HashMap<Integer, Boolean>(points);
	}

	protected AbstractKey(AbstractKey copy)
	{
		this.points = copy.points;
	}

	protected Map<Integer, Boolean> getPoints()
	{
		return Collections.unmodifiableMap(points);
	}
	
	protected void internalMutate(double deviation)
	{
		Integer[] pointsArray = new Integer[this.points.size()];
		this.points.keySet().toArray(pointsArray);
		
		MutableInteger point = new MutableInteger(pointsArray[RANDOM.nextInt(pointsArray.length)]);
		if(RANDOM.nextBoolean())
			this.points.put(point.mutate(deviation).getNumber(), RANDOM.nextBoolean());
		else
			this.points.remove(point.getNumber());
	}

	@Override
	public int hashCode()
	{
		return this.points.hashCode();
	}

	@Override
	public boolean equals(Object compareWith)
	{
		return this.points.equals(compareWith);
	}
	
	@Override
	public AbstractKey clone()
	{
		try
		{
			AbstractKey copy = (AbstractKey) super.clone();
			copy.points = this.points;
			return copy;
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("CloneNotSupportedException caught but not expected!", caught);
			throw new AssertionError("CloneNotSupportedException caught but not expected: " + caught);
		}
	}

	public abstract AbstractKey mutate(double deviation);
}
