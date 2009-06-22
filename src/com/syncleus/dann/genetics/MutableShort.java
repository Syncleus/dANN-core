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

public class MutableShort extends MutableNumber<MutableShort, Short> implements Comparable<MutableShort>
{
	public MutableShort(short value)
	{
		super(Short.valueOf(value));
	}

	public MutableShort(String s)
	{
		super(Short.valueOf(s));
	}

	public MutableShort(Short value)
	{
		super(value);
	}

	public MutableShort clone()
	{
		return new MutableShort(this.getNumber());
	}

	public MutableShort mutate(double deviation)
	{
		double doubleDistributed = MutableNumber.getDistributedRandom(deviation);
		short distributedRand = (short) doubleDistributed;
		if(doubleDistributed > Short.MAX_VALUE)
			distributedRand = Short.MAX_VALUE;
		else if(doubleDistributed < Short.MIN_VALUE)
			distributedRand = Short.MIN_VALUE;

		short result = (short)(this.getNumber().shortValue() + distributedRand);

		if(( distributedRand > 0)&&( result < this.getNumber().shortValue()))
			return new MutableShort(Short.MAX_VALUE);
		else if((distributedRand < 0)&&( result > this.getNumber().shortValue()))
			return new MutableShort(Short.MIN_VALUE);

		return new MutableShort(result);
	}

	public int compareTo(MutableShort compareWith)
	{
		return this.getNumber().compareTo(compareWith.getNumber());
	}
}
