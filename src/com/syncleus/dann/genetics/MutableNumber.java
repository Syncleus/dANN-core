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

import java.util.Random;

public abstract class MutableNumber<E extends MutableNumber, N extends Number> extends Number implements Mutable
{
	private static Random random = new Random();

	private N number;

	protected MutableNumber(N number)
	{
		this.number = number;
	}

	static protected double getDistributedRandom(double deviation)
	{
		double normalRand = (MutableNumber.random.nextDouble() * 2.0) - 1.0;
		return atanh(normalRand) * Math.abs(deviation);
	}

    static private double atanh(double value)
    {
        return 0.5 * Math.log(Math.abs((value + 1.0) / (1.0 - value)));
    }

	public N getNumber()
	{
		return this.number;
	}

	public double doubleValue()
	{
		return this.number.doubleValue();
	}

	public float floatValue()
	{
		return this.number.floatValue();
	}
	
	public byte byteValue()
	{
		return this.number.byteValue();
	}

	public short shortValue()
	{
		return this.number.shortValue();
	}

	public int intValue()
	{
		return this.number.intValue();
	}

	public long longValue()
	{
		return this.number.longValue();
	}

	@Override
	public int hashCode()
	{
		return this.number.hashCode();
	}

	@Override
	public boolean equals(Object compareWith)
	{
		return this.number.equals(compareWith);
	}

	@Override
	public String toString()
	{
		return this.number.toString();
	}

	@Override
	public abstract MutableNumber<E,N> clone();
	public abstract MutableNumber<E,N> mutate(double deviation);
}
