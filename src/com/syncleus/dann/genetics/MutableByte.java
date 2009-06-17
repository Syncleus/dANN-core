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

public class MutableByte extends MutableNumber<MutableByte, Byte> implements Comparable<MutableByte>
{
	public MutableByte(byte value)
	{
		super(Byte.valueOf(value));
	}

	public MutableByte(String s)
	{
		super(Byte.valueOf(s));
	}

	public MutableByte(Byte value)
	{
		super(value);
	}

	public MutableByte mutate(double deviation)
	{
		double doubleDistributed = MutableNumber.getDistributedRandom(deviation);
		byte distributedRand = (byte) doubleDistributed;
		if(doubleDistributed > Byte.MAX_VALUE)
			distributedRand = Byte.MAX_VALUE;
		else if(doubleDistributed < Byte.MIN_VALUE)
			distributedRand = Byte.MIN_VALUE;

		byte result = (byte) (this.getNumber().byteValue() + distributedRand);

		if(( distributedRand > 0)&&( result < this.getNumber().byteValue()))
			return new MutableByte(Byte.MAX_VALUE);
		else if((distributedRand < 0)&&( result > this.getNumber().byteValue()))
			return new MutableByte(Byte.MIN_VALUE);

		return new MutableByte(result);
	}

	public int compareTo(MutableByte compareWith)
	{
		return this.getNumber().compareTo(compareWith.getNumber());
	}
}
