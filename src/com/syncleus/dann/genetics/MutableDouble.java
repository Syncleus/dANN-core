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
package com.syncleus.dann.genetics;

public class MutableDouble extends MutableNumber<MutableDouble, Double> implements Comparable<MutableDouble>
{
	public MutableDouble(double value)
	{
		super(Double.valueOf(value));
	}

	public MutableDouble(String s)
	{
		super(Double.valueOf(s));
	}

	public MutableDouble(Double value)
	{
		super(value);
	}

	public MutableDouble clone()
	{
		return new MutableDouble(this.getNumber());
	}

	public MutableDouble mutate(double deviation)
	{
		double distributedRand = MutableNumber.getDistributedRandom(deviation);

		double result = this.getNumber().doubleValue() + distributedRand;

		if(Double.isInfinite(result))
		{
			if(result > 0)
				result = Double.MAX_VALUE;
			else
				result = Double.MAX_VALUE * 1.0;
		}

		return new MutableDouble(result);
	}

	public int compareTo(MutableDouble compareWith)
	{
		return this.getNumber().compareTo(compareWith.getNumber());
	}
}
