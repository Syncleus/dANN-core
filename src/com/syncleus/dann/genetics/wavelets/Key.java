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

import com.syncleus.dann.genetics.MutableInteger;
import java.util.*;

public abstract class Key
{
	private HashMap<Integer, Boolean> points;
	private static Random random = new Random();

	protected Key()
	{
		this.points = new HashMap<Integer, Boolean>();
		this.points.put(Integer.valueOf(random.nextInt()), random.nextBoolean());
	}

	protected Key(Map<Integer, Boolean> points)
	{
		this.points = new HashMap<Integer, Boolean>(points);
	}

	protected Key(Key copy)
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
		
		MutableInteger point = new MutableInteger(pointsArray[random.nextInt(pointsArray.length)]);
		if(random.nextBoolean())
			this.points.put(point.mutate(deviation).getNumber(), random.nextBoolean());
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
	public abstract Key clone();
	public abstract Key mutate(double deviation);
}
