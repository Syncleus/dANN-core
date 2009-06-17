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

public class MutableInteger extends MutableNumber<MutableInteger, Integer> implements Comparable<MutableInteger>
{
	public MutableInteger(int value)
	{
		super(Integer.valueOf(value));
	}

	public MutableInteger(String s)
	{
		super(Integer.valueOf(s));
	}

	public MutableInteger(Integer value)
	{
		super(value);
	}

	public MutableInteger mutate(double deviation)
	{
		double doubleDistributed = MutableNumber.getDistributedRandom(deviation);
		int distributedRand = (int) doubleDistributed;
		if(doubleDistributed > Integer.MAX_VALUE)
			distributedRand = Integer.MAX_VALUE;
		else if(doubleDistributed < Integer.MIN_VALUE)
			distributedRand = Integer.MIN_VALUE;

		int result = this.getNumber().intValue() + distributedRand;

		if(( distributedRand > 0)&&( result < this.getNumber().intValue()))
			return new MutableInteger(Integer.MAX_VALUE);
		else if((distributedRand < 0)&&( result > this.getNumber().intValue()))
			return new MutableInteger(Integer.MIN_VALUE);

		return new MutableInteger(result);
	}

	public int compareTo(MutableInteger compareWith)
	{
		return this.getNumber().compareTo(compareWith.getNumber());
	}
}
