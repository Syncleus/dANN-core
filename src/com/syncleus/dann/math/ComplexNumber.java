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
/*
** Derived from the Public-Domain sources found at
** http://www.cs.princeton.edu/introcs/97data/ as of 9/13/2009.
*/
package com.syncleus.dann.math;

import java.util.*;

public class ComplexNumber implements TrigonometricAlgebraic<ComplexNumber>
{
	public static final class Field implements com.syncleus.dann.math.OrderedField<ComplexNumber>
	{
		public final static Field FIELD = new Field();

		private Field()
		{
		}

		public ComplexNumber getZero()
		{
			return ComplexNumber.ZERO;
		}

		public ComplexNumber getOne()
		{
			return ComplexNumber.ONE;
		}

		public static ComplexNumber getImaginaryUnit()
		{
			return ComplexNumber.I;
		}
	}

	public final static ComplexNumber ONE = new ComplexNumber(1, 0);
	public final static ComplexNumber ZERO = new ComplexNumber(0, 0);
	public final static ComplexNumber I = new ComplexNumber(0, 1);
	private final double realValue;
	private final double imaginaryValue;

	public ComplexNumber(final double real, final double imaginary)
	{
		this.realValue = real;
		this.imaginaryValue = imaginary;
	}

	public ComplexNumber(final double imaginary)
	{
		this.realValue = 0.0;
		this.imaginaryValue = imaginary;
	}

	public com.syncleus.dann.math.Field<ComplexNumber> getField()
	{
		return Field.FIELD;
	}

	public final double absScalar()
	{
		return Math.hypot(this.realValue, this.imaginaryValue);
	}

	public final ComplexNumber abs()
	{
		return new ComplexNumber(this.absScalar(), 0.0);
	}

	//Value between -pi and pi
	public final double phase()
	{
		return Math.atan2(this.imaginaryValue, this.realValue);
	}

	public final ComplexNumber add(final ComplexNumber value)
	{
		return new ComplexNumber(this.realValue + value.realValue, this.imaginaryValue + value.imaginaryValue);
	}

	public final ComplexNumber add(final double value)
	{
		return this.add(new ComplexNumber(value, 0.0));
	}

	public final ComplexNumber subtract(final ComplexNumber value)
	{
		return new ComplexNumber(this.realValue - value.realValue, this.imaginaryValue - value.imaginaryValue);
	}

	public final ComplexNumber subtract(final double value)
	{
		return this.subtract(new ComplexNumber(value, 0.0));
	}

	public final ComplexNumber multiply(final ComplexNumber value)
	{
		final double imaginary = this.realValue * value.imaginaryValue + this.imaginaryValue * value.realValue;
		final double real = this.realValue * value.realValue - this.imaginaryValue * value.imaginaryValue;
		return new ComplexNumber(real, imaginary);
	}

	public final ComplexNumber multiply(final double value)
	{
		return new ComplexNumber(value * this.realValue, value * this.imaginaryValue);
	}

	public final ComplexNumber divide(final ComplexNumber value)
	{
		return this.multiply(value.reciprocal());
	}

	public final ComplexNumber divide(final double value)
	{
		return this.divide(new ComplexNumber(value, 0.0));
	}

	public final ComplexNumber exp()
	{
		return new ComplexNumber(Math.exp(this.realValue) * Math.cos(this.imaginaryValue), Math.exp(this.realValue) * Math.sin(this.imaginaryValue));
	}

	public final ComplexNumber log()
	{
		return new ComplexNumber(Math.log(this.absScalar()), Math.atan2(this.imaginaryValue, this.realValue));
	}

	public final ComplexNumber pow(final ComplexNumber exponent)
	{
		if (exponent == null)
			throw new IllegalArgumentException("exponent can not be null");
		return this.log().multiply(exponent).exp();
	}

	public final ComplexNumber pow(final double exponent)
	{
		return this.log().multiply(exponent).exp();
	}

	public List<ComplexNumber> root(final int n)
	{
		if (n <= 0)
			throw new IllegalArgumentException("n must be greater than 0");
		final List<ComplexNumber> result = new ArrayList<ComplexNumber>();
		double inner = this.phase() / n;
		for(int nIndex = 0; nIndex < n; nIndex++)
		{
			result.add(new ComplexNumber(Math.cos(inner) * Math.pow(this.absScalar(), 1.0 / n), Math.sin(inner) * Math.pow(this.absScalar(), 1.0 / n)));
			inner += 2 * Math.PI / n;
		}
		return Collections.unmodifiableList(result);
	}

	public final ComplexNumber sqrt()
	{
		//The square-root of the complex number (a + i b) is
		//sqrt(a + i b) = +/- (sqrt(radius + a) + i sqrt(radius - a) sign(b)) sqrt(2) / 2,
		//where radius = sqrt(a^2 + b^2).
		final double r = Math.sqrt((this.realValue * this.realValue) + (this.imaginaryValue * this.imaginaryValue));
		final ComplexNumber intermediate = new ComplexNumber(Math.sqrt(r + this.realValue), Math.sqrt(r + this.realValue) * Math.signum(this.imaginaryValue));
		return intermediate.multiply(Math.sqrt(2.0)).divide(2.0);
	}

	private ComplexNumber sqrt1Minus()
	{
		return (new ComplexNumber(1.0, 0.0)).subtract(this.multiply(this)).sqrt();
	}

	public ComplexNumber hypot(final ComplexNumber operand)
	{
		return this.pow(2.0).add(operand.pow(2.0)).sqrt();
	}

	public final ComplexNumber sin()
	{
		return new ComplexNumber(Math.sin(this.realValue) * Math.cosh(this.imaginaryValue), Math.cos(this.realValue) * Math.sinh(this.imaginaryValue));
	}

	public final ComplexNumber asin()
	{
		return sqrt1Minus().add(this.multiply(ComplexNumber.I)).log().multiply(ComplexNumber.I.negate());
	}

	public final ComplexNumber sinh()
	{
		return new ComplexNumber(Math.sinh(this.realValue) * Math.cos(this.imaginaryValue), Math.cosh(this.realValue) * Math.sin(this.imaginaryValue));
	}

	public final ComplexNumber cos()
	{
		return new ComplexNumber(Math.cos(this.realValue) * Math.cosh(this.imaginaryValue), -Math.sin(this.realValue) * Math.sinh(this.imaginaryValue));
	}

	public final ComplexNumber acos()
	{
		return this.add(this.sqrt1Minus().multiply(ComplexNumber.I)).log().multiply(ComplexNumber.I.negate());
	}

	public final ComplexNumber cosh()
	{
		return new ComplexNumber(Math.cosh(this.realValue) * Math.cos(this.imaginaryValue), Math.sinh(this.realValue) * Math.sin(this.imaginaryValue));
	}

	public final ComplexNumber tan()
	{
		return sin().divide(cos());
	}

	public final ComplexNumber atan()
	{
		return this.add(ComplexNumber.I).divide(ComplexNumber.I.subtract(this)).log().multiply(ComplexNumber.I.divide(new ComplexNumber(2.0, 0.0)));
	}

	public final ComplexNumber tanh()
	{
		final double denominator = Math.cosh(2.0 * this.realValue) + Math.cos(2.0 * this.imaginaryValue);
		return new ComplexNumber(Math.sinh(2.0 * this.realValue) / denominator, Math.sin(2.0 * this.imaginaryValue) / denominator);
	}

	public final ComplexNumber negate()
	{
		return new ComplexNumber(-this.realValue, -this.imaginaryValue);
	}

	public final ComplexNumber reciprocal()
	{
		final double scale = (this.realValue * this.realValue) + (this.imaginaryValue * this.imaginaryValue);
		return new ComplexNumber(this.realValue / scale, -this.imaginaryValue / scale);
	}

	public final ComplexNumber conjugate()
	{
		return new ComplexNumber(this.realValue, -this.imaginaryValue);
	}

	public boolean isNaN()
	{
		return Double.isNaN(this.realValue) || Double.isNaN(this.imaginaryValue);
	}

	public boolean isInfinite()
	{
		return Double.isInfinite(this.realValue) || Double.isInfinite(this.imaginaryValue);
	}

	public final double getRealValue()
	{
		return this.realValue;
	}

	public final double getImaginaryValue()
	{
		return this.imaginaryValue;
	}

	@Override
	public int hashCode()
	{
		final int imaginaryHash = Double.valueOf(this.imaginaryValue).hashCode();
		final int realHash = Double.valueOf(this.realValue).hashCode();
		return (imaginaryHash * realHash) + realHash;
	}

	@Override
	public boolean equals(final Object compareObject)
	{
		if (!(compareObject instanceof ComplexNumber))
			return false;
		final ComplexNumber compareComplex = (ComplexNumber) compareObject;
		if (compareComplex.realValue != this.realValue)
			return false;
		if (compareComplex.imaginaryValue != this.imaginaryValue)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		if (this.imaginaryValue == 0)
			return this.realValue + "0";
		if (this.realValue == 0)
			return this.imaginaryValue + "i";
		if (this.imaginaryValue < 0)
			return this.realValue + " - " + -this.imaginaryValue + 'i';
		return this.realValue + " + " + this.imaginaryValue + 'i';
	}

	public static ComplexNumber scalarToComplex(final double scalar)
	{
		return new ComplexNumber(scalar, 0.0);
	}

	public static ComplexNumber scalarToComplex(final float scalar)
	{
		return new ComplexNumber(scalar, 0.0);
	}

	public static ComplexNumber scalarToComplex(final long scalar)
	{
		return new ComplexNumber(scalar, 0.0);
	}

	public static ComplexNumber scalarToComplex(final int scalar)
	{
		return new ComplexNumber(scalar, 0.0);
	}

	public static ComplexNumber scalarToComplex(final short scalar)
	{
		return new ComplexNumber(scalar, 0.0);
	}

	public static ComplexNumber polarToComplex(final double radius, final double theta)
	{
		if (radius < 0)
			throw new IllegalArgumentException("r must be greater than or equal to 0");
		return new ComplexNumber(Math.cos(theta) * radius, Math.sin(theta) * radius);
	}

	public static ComplexNumber sum(final ComplexNumber... values)
	{
		ComplexNumber complexSum = ComplexNumber.ZERO;
		for(final ComplexNumber value : values)
			complexSum = complexSum.add(value);
		return complexSum;
	}

	public static ComplexNumber multiply(final ComplexNumber... values)
	{
		ComplexNumber complexProduct = new ComplexNumber(1.0, 0.0);
		for(final ComplexNumber value : values)
			complexProduct = complexProduct.multiply(value);
		return complexProduct;
	}
}
