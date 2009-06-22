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

/**
 * This is a MutableNumber backed by a Byte. It is essentially a byte
 * implementing mutable.
 *
 * @author Syncleus, Inc.
 * @since 2.0
 *
 */
public class MutableByte extends MutableNumber<Byte> implements Comparable<MutableByte>
{
	/**
	 * Initializes a new instance of this class with the specified value.
	 *
	 * @param value The value of this number.
	 * @since 2.0
	 */
	public MutableByte(byte value)
	{
		super(Byte.valueOf(value));
	}

	/**
	 * Initializes a new instance of this class from the value represented
	 * by the specified string.
	 *
	 * @param s A string representing the value of this number.
	 * @since 2.0
	 */
	public MutableByte(String s)
	{
		super(Byte.valueOf(s));
	}

	/**
	 * Initializes a new instance of this class as a copy of the specefied
	 * number.
	 *
	 * @param value The value to copy
	 * @since 2.0
	 */
	public MutableByte(Byte value)
	{
		super(value);
	}

	/**
	 * An new exact copy of this object with the same value.
	 *
	 * @return a new exact copy of this object with the same value.
	 * @since 2.0
	 */
	public MutableByte clone()
	{
		return new MutableByte(this.getNumber());
	}

	/**
	 * This will make a copy of the object and mutate it. The mutation has
	 * a normal distribution multiplied by the deviation. If the Number is
	 * mutated past its largest or smallest representable number it will
	 * simply return the max or min respectivly.
	 *
	 * @param deviation A double indicating how extreme the mutation will be.
	 * The greater the deviation the more drastically the object will mutate.
	 * A deviation of 0 should cause no mutation.
	 * @return A copy of the current object with potential mutations.
	 * @since 2.0
	 */
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

	/**
	 * Compares the value of this number against another object of the same
	 * type. The backing number handles the comparison.
	 *
	 * @param compareWith Number to compare against.
	 * @return the natural ordering of the backed number.
	 * @since 2.0
	 */
	public int compareTo(MutableByte compareWith)
	{
		return this.getNumber().compareTo(compareWith.getNumber());
	}
}
