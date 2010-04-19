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


import com.syncleus.dann.UnexpectedDannError;
import java.util.*;
import org.apache.log4j.Logger;
import com.syncleus.dann.genetics.MutableInteger;

public abstract class AbstractKey implements Cloneable
{
	private Map<Integer, Boolean> points;
	private final static Random RANDOM = Mutations.getRandom();
	private final static Logger LOGGER = Logger.getLogger(AbstractKey.class);

	protected AbstractKey()
	{
		HashMap<Integer, Boolean> newPoints = new HashMap<Integer, Boolean>();
		newPoints.put(Integer.valueOf(RANDOM.nextInt()), RANDOM.nextBoolean());
		this.points = Collections.unmodifiableMap(newPoints);
	}

	protected AbstractKey(Map<Integer, Boolean> points)
	{
		if(points.isEmpty())
			throw new IllegalArgumentException("points must have atleast one entry");

		this.points = Collections.unmodifiableMap(new HashMap<Integer, Boolean>(points));
	}

	protected AbstractKey(String keyString)
	{
		HashMap<Integer, Boolean> newPoints = new HashMap<Integer, Boolean>();

		final char[] keyChars = keyString.toCharArray();
		int index = 0;
		for(char keyChar : keyChars)
		{
			if((keyChar != '1')&&(keyChar != '0')&&(keyChar != 'x'))
				throw new IllegalArgumentException("eyString is only allowed to contain the following characters: 1, 0, x");

			if(keyChar == '1')
				newPoints.put(index, Boolean.TRUE);
			else if(keyChar == '0')
				newPoints.put(index, Boolean.FALSE);

			index++;
		}

		if(newPoints.isEmpty())
			throw new IllegalArgumentException("string must contain atleast one 1, or 0 point");

		this.points = Collections.unmodifiableMap(newPoints);
	}

	protected AbstractKey(AbstractKey copy)
	{
		this.points = copy.points;
	}

	protected Map<Integer, Boolean> getPoints()
	{
		return this.points;
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
	public String toString()
	{
		final SortedSet<Integer> orderedIndexes = new TreeSet<Integer>(this.points.keySet());

		final Integer lowIndex = orderedIndexes.first();
		final Integer highIndex = orderedIndexes.last();

		final StringBuilder outBuilder = new StringBuilder();
		for(Integer index = lowIndex; index <= highIndex; index++)
		{
			if(orderedIndexes.contains(index))
			{
				final boolean indexPoint = this.points.get(index);
				if(indexPoint)
					outBuilder.append('1');
				else
					outBuilder.append('0');
			}
			else
				outBuilder.append('x');
		}

		return outBuilder.toString();
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
			throw new UnexpectedDannError("CloneNotSupportedException caught but not expected", caught);
		}
	}

	public AbstractKey mutate(double deviation)
	{
		Integer[] pointsArray = new Integer[this.points.size()];
		this.points.keySet().toArray(pointsArray);

		MutableInteger point = new MutableInteger(pointsArray[RANDOM.nextInt(pointsArray.length)]);
		final Map<Integer, Boolean> newPoints = new HashMap(this.points);
		if(RANDOM.nextBoolean())
			newPoints.put(point.mutate(deviation).getNumber(), RANDOM.nextBoolean());
		else
			newPoints.remove(point.getNumber());

		AbstractKey copy = (AbstractKey) this.clone();
		copy.points = Collections.unmodifiableMap(newPoints);
		return copy;
	}
}
