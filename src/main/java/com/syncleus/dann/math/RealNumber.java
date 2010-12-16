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
package com.syncleus.dann.math;

import java.util.*;

public class RealNumber extends Number implements OrderedTrigonometricAlgebraic<RealNumber>
{
	private static final long serialVersionUID = -1417799779268676071L;

	public static final class Field implements com.syncleus.dann.math.OrderedField<RealNumber>
	{
		public static final Field FIELD = new Field();

		private Field()
		{
		}

		public RealNumber getZero()
		{
			return RealNumber.ZERO;
		}

		public RealNumber getOne()
		{
			return RealNumber.ONE;
		}
	}

	public static final RealNumber ZERO = new RealNumber(0);
	public static final RealNumber ONE = new RealNumber(1);
	private final double value;

	public RealNumber(final double value)
	{
		this.value = value;
	}

	public double getValue()
	{
		return this.value;
	}

	public com.syncleus.dann.math.OrderedField<RealNumber> getField()
	{
		return Field.FIELD;
	}

	public RealNumber max(final RealNumber maxValue)
	{
		if( this.compareTo(maxValue) > 0 )
			return this;
		return maxValue;
	}

	public RealNumber min(final RealNumber minValue)
	{
		if( this.compareTo(minValue) < 0 )
			return this;
		return minValue;
	}

	public RealNumber add(final RealNumber operand)
	{
		return new RealNumber(this.value + operand.value);
	}

	public RealNumber add(final double operand)
	{
		return new RealNumber(this.value + operand);
	}

	public RealNumber subtract(final RealNumber operand)
	{
		return new RealNumber(this.value - operand.value);
	}

	public RealNumber subtract(final double operand)
	{
		return new RealNumber(this.value - operand);
	}

	public RealNumber multiply(final RealNumber operand)
	{
		return new RealNumber(this.value * operand.value);
	}

	public RealNumber multiply(final double operand)
	{
		return new RealNumber(this.value * operand);
	}

	public RealNumber divide(final RealNumber operand)
	{
		return new RealNumber(this.value / operand.value);
	}

	public RealNumber divide(final double operand)
	{
		return new RealNumber(this.value / operand);
	}

	public RealNumber negate()
	{
		return this.multiply(-1.0);
	}

	public RealNumber reciprocal()
	{
		return new RealNumber(1.0 / this.value);
	}

	public RealNumber abs()
	{
		if( this.value < 0.0 )
			return new RealNumber(Math.abs(this.value));
		else
			return this;
	}

	public RealNumber exp()
	{
		return new RealNumber(Math.exp(this.value));
	}

	public RealNumber log()
	{
		return new RealNumber(Math.log(this.value));
	}

	public RealNumber pow(final RealNumber exponent)
	{
		return new RealNumber(Math.pow(this.value, exponent.value));
	}

	public RealNumber pow(final double exponent)
	{
		return new RealNumber(Math.pow(this.value, exponent));
	}

	public List<RealNumber> root(final int n)
	{
		final List<RealNumber> roots = new ArrayList<RealNumber>();
		final double positiveRoot = Math.pow(this.value, 1.0 / ((double) n));
		roots.add(new RealNumber(positiveRoot));
		if( n % 2 == 0 )
			roots.add(new RealNumber(-positiveRoot));
		return Collections.unmodifiableList(roots);
	}

	public RealNumber sqrt()
	{
		return new RealNumber(Math.sqrt(this.value));
	}

	public RealNumber hypot(final RealNumber operand)
	{
		return new RealNumber(Math.hypot(this.value, operand.value));
	}

	public RealNumber sin()
	{
		return new RealNumber(Math.sin(this.value));
	}

	public RealNumber asin()
	{
		return new RealNumber(Math.asin(this.value));
	}

	public RealNumber sinh()
	{
		return new RealNumber(Math.sinh(this.value));
	}

	public RealNumber cos()
	{
		return new RealNumber(Math.cos(this.value));
	}

	public RealNumber acos()
	{
		return new RealNumber(Math.acos(this.value));
	}

	public RealNumber cosh()
	{
		return new RealNumber(Math.cosh(this.value));
	}

	public RealNumber tan()
	{
		return new RealNumber(Math.tan(this.value));
	}

	public RealNumber atan()
	{
		return new RealNumber(Math.atan(this.value));
	}

	public RealNumber tanh()
	{
		return new RealNumber(Math.tanh(this.value));
	}

	@Override
	public short shortValue()
	{
		return (short) this.value;
	}

	public int intValue()
	{
		return (int) this.value;
	}

	public long longValue()
	{
		return (long) this.value;
	}

	public float floatValue()
	{
		return (float) this.value;
	}

	public double doubleValue()
	{
		return this.value;
	}

	@Override
	public int hashCode()
	{
		return Double.valueOf(this.value).hashCode();
	}

	@Override
	public boolean equals(final Object compareObj)
	{
		if( !(compareObj instanceof RealNumber) )
			return false;
		final RealNumber compareWith = (RealNumber) compareObj;
		return (compareWith.value == this.value);
	}

	public int compareTo(final RealNumber compareWith)
	{
		if( this.value > compareWith.value )
			return 1;
		else if( this.value < compareWith.value )
			return -1;
		return 0;
	}
}
