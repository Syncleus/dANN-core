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

public class MutableLong extends MutableNumber<MutableLong, Long> implements Comparable<MutableLong>
{
	public MutableLong(long value)
	{
		super(Long.valueOf(value));
	}

	public MutableLong(String s)
	{
		super(Long.valueOf(s));
	}

	public MutableLong(Long value)
	{
		super(value);
	}

	public MutableLong mutate(double deviation)
	{
		double doubleDistributed = MutableNumber.getDistributedRandom(deviation);
		long distributedRand = (long) doubleDistributed;
		if(doubleDistributed > Long.MAX_VALUE)
			distributedRand = Long.MAX_VALUE;
		else if(doubleDistributed < Long.MIN_VALUE)
			distributedRand = Long.MIN_VALUE;

		long result = this.getNumber().longValue() + distributedRand;

		if(( distributedRand > 0)&&( result < this.getNumber().longValue()))
			return new MutableLong(Long.MAX_VALUE);
		else if((distributedRand < 0)&&( result > this.getNumber().longValue()))
			return new MutableLong(Long.MIN_VALUE);

		return new MutableLong(result);
	}

	public int compareTo(MutableLong compareWith)
	{
		return this.getNumber().compareTo(compareWith.getNumber());
	}
}
