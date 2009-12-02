/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RealNumber extends Number implements OrderedTrigonometricAlgebraic<RealNumber>
{
	public static final class Field implements com.syncleus.dann.math.OrderedField<RealNumber>
	{
		public final static Field FIELD = new Field();

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

	public final static RealNumber ZERO = new RealNumber(0);
	public final static RealNumber ONE = new RealNumber(1);
	private final double value;

	public RealNumber(double value)
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

	public RealNumber max(RealNumber value)
	{
		if(this.compareTo(value) > 0)
			return this;
		return value;
	}

	public RealNumber min(RealNumber value)
	{
		if(this.compareTo(value) < 0)
			return this;
		return value;
	}

	public RealNumber add(RealNumber operand)
	{
		return new RealNumber(this.value + operand.value);
	}

	public RealNumber add(double operand)
	{
		return new RealNumber(this.value + operand);
	}

	public RealNumber subtract(RealNumber operand)
	{
		return new RealNumber(this.value - operand.value);
	}

	public RealNumber subtract(double operand)
	{
		return new RealNumber(this.value - operand);
	}

	public RealNumber multiply(RealNumber operand)
	{
		return new RealNumber(this.value * operand.value);
	}

	public RealNumber multiply(double operand)
	{
		return new RealNumber(this.value * operand);
	}

	public RealNumber divide(RealNumber operand)
	{
		return new RealNumber(this.value / operand.value);
	}

	public RealNumber divide(double operand)
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
		if(this.value < 0.0)
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

	public RealNumber pow(RealNumber exponent)
	{
		return new RealNumber(Math.pow(this.value, exponent.value));
	}

	public RealNumber pow(double exponent)
	{
		return new RealNumber(Math.pow(this.value, exponent));
	}

	public List<RealNumber> root(int n)
	{
		List<RealNumber> roots = new ArrayList<RealNumber>();
		double positiveRoot = Math.pow(this.value, 1.0/((double)n));
		roots.add(new RealNumber(positiveRoot));
		if(n%2 == 0)
			roots.add(new RealNumber(-positiveRoot));
		return Collections.unmodifiableList(roots);
	}

	public RealNumber sqrt()
	{
		return new RealNumber(Math.sqrt(this.value));
	}

	public RealNumber hypot(RealNumber operand)
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
	public boolean equals(Object compareObj)
	{
		if(!(compareObj instanceof RealNumber))
			return false;

		RealNumber compareWith = (RealNumber) compareObj;

		if(compareWith.value == this.value)
			return true;
		return false;
	}

	public int compareTo(RealNumber compareWith)
	{
		if(this.getValue() > compareWith.getValue())
			return 1;
		else if(this.getValue() < compareWith.getValue())
			return -1;
		return 0;
	}
}
