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

public class MutableInteger extends MutableNumber<MutableInteger, Integer>
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
		double distributedRand = MutableNumber.getDistributedRandom(deviation);

		if ((Integer.MAX_VALUE - distributedRand)< this.getNumber().intValue())
		{
			if(distributedRand > 0)
				return new MutableInteger(Integer.MAX_VALUE);
			else
				return new MutableInteger(Integer.MIN_VALUE);
		}

		return new MutableInteger(this.getNumber().intValue() + ((int)distributedRand));
	}
}
