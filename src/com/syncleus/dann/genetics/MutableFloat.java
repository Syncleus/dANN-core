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

public class MutableFloat extends MutableNumber<MutableFloat, Float> implements Comparable<MutableFloat>
{
	public MutableFloat(float value)
	{
		super(Float.valueOf(value));
	}

	public MutableFloat(String s)
	{
		super(Float.valueOf(s));
	}

	public MutableFloat(Float value)
	{
		super(value);
	}

	public MutableFloat clone()
	{
		return new MutableFloat(this.getNumber());
	}

	public MutableFloat mutate(double deviation)
	{
		double doubleDistributed = MutableNumber.getDistributedRandom(deviation);
		float distributedRand = (float) doubleDistributed;
		if(doubleDistributed > Float.MAX_VALUE)
			distributedRand = Float.MAX_VALUE;
		else if(doubleDistributed < (Float.MAX_VALUE * -1f))
			distributedRand = Float.MAX_VALUE * -1f;

		float result = this.getNumber().floatValue() + distributedRand;

		if(( distributedRand > 0f)&&( result < this.getNumber().floatValue()))
			return new MutableFloat(Float.MAX_VALUE);
		else if((distributedRand < 0f)&&( result > this.getNumber().floatValue()))
			return new MutableFloat(Float.MAX_VALUE * -1f);

		return new MutableFloat(result);
	}

	public int compareTo(MutableFloat compareWith)
	{
		return this.getNumber().compareTo(compareWith.getNumber());
	}
}
