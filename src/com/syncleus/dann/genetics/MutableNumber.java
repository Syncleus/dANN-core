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

import com.syncleus.dann.genetics.wavelets.Mutation;
import java.util.Random;

/**
 * An abstract class representing a gene which expresses a constant value
 * which only changes through mutation.
 *
 * @author Syncleus, Inc.
 * @param <N> The type of number backing the Gene.
 * @since 2.0
 *
 */
public abstract class MutableNumber<N extends Number> extends Number
{
	private static Random random = Mutation.getRandom();

	private N number;

	/**
	 * Initializes a new MutableNumber backed by the specified number.
	 *
	 * @param number The number to back this MutableNumber
	 * @since 2.0
	 */
	protected MutableNumber(N number)
	{
		this.number = number;
	}

	/**
	 * Returns a random double normally distributed around 0.0 and multiplied
	 * by deviation.
	 *
	 * @param deviation multiplier for the distribution.
	 * @return Random double with the appropriate distribution.
	 * @since 2.0
	 */
	static protected double getDistributedRandom(double deviation)
	{
		double normalRand = (MutableNumber.random.nextDouble() * 2.0) - 1.0;
		return atanh(normalRand) * Math.abs(deviation);
	}

    static private double atanh(double value)
    {
        return 0.5 * Math.log(Math.abs((value + 1.0) / (1.0 - value)));
    }

	/**
	 * Get the number used to back this object.
	 *
	 * @return The backing number.
	 * @since 2.0
	 */
	public N getNumber()
	{
		return this.number;
	}

	/**
	 * double value representation of the number.
	 *
	 * @return double representation of the value.
	 * @since 2.0
	 */
	public double doubleValue()
	{
		return this.number.doubleValue();
	}

	/**
	 * float value representation of the number.
	 *
	 * @return float representation of the value.
	 * @since 2.0
	 */
	public float floatValue()
	{
		return this.number.floatValue();
	}

	/**
	 * byte value representation of the number.
	 *
	 * @return byte representation of the value.
	 * @since 2.0
	 */
	@Override
	public byte byteValue()
	{
		return this.number.byteValue();
	}

	/**
	 * byte value representation of the number.
	 *
	 * @return byte representation of the value.
	 * @since 2.0
	 */
	@Override
	public short shortValue()
	{
		return this.number.shortValue();
	}

	/**
	 * int value representation of the number.
	 *
	 * @return int representation of the value.
	 * @since 2.0
	 */
	public int intValue()
	{
		return this.number.intValue();
	}

	/**
	 * long value representation of the number.
	 *
	 * @return long representation of the value.
	 * @since 2.0
	 */
	public long longValue()
	{
		return this.number.longValue();
	}

	/**
	 * The hashCode used will be the same as the backing number.
	 *
	 * @return hashCode of the backing number.
	 * @since 2.0
	 */
	@Override
	public int hashCode()
	{
		return this.number.hashCode();
	}

	/**
	 * Checks equals using the equals of the backing number.
	 *
	 * @param compareWith object to check if it is equal to this object.
	 * @return equals as reported by the backing number.
	 * @since 2.0
	 */
	@Override
	public boolean equals(Object compareWith)
	{
		return this.number.equals(compareWith);
	}

	/**
	 * String representation of the backing number.
	 *
	 * @return String representation of the backing number.
	 * @since 2.0
	 */
	@Override
	public String toString()
	{
		return this.number.toString();
	}

	/**
	 * All children of this class should override this method and return
	 * their own class type even if it is abstract. It should return a copy
	 * without any mutation.
	 *
	 * @return an exact copy of this object.
	 * @since 2.0
	 */
	@Override
	public abstract MutableNumber<N> clone();

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
	public abstract MutableNumber<N> mutate(double deviation);
}
