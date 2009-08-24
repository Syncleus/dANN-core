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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class ComplexNumber
{
	public final static ComplexNumber ZERO = new ComplexNumber(0, 0);
	public final static ComplexNumber I = new ComplexNumber(0, 1);

	private final double realValue;
	private final double imaginaryValue;

	public ComplexNumber(double real, double imaginary)
	{
		realValue = real;
		imaginaryValue = imaginary;
	}

	public final double abs()
	{
		return Math.hypot(realValue, imaginaryValue);
	}

	//Value between -pi and pi
	public final double phase()
	{
		return Math.atan2(imaginaryValue, realValue);
	}

	public final ComplexNumber add(ComplexNumber value)
	{
		return new ComplexNumber(this.realValue + value.realValue, this.imaginaryValue + value.imaginaryValue);
	}

	public final ComplexNumber add(double value)
	{
		return this.add(new ComplexNumber(value, 0.0));
	}

	public final ComplexNumber subtract(ComplexNumber value)
	{
		return new ComplexNumber(this.realValue - value.realValue, this.imaginaryValue - value.imaginaryValue);
	}

	public final ComplexNumber subtract(double value)
	{
		return this.subtract(new ComplexNumber(value, 0.0));
	}

	public final ComplexNumber multiply(ComplexNumber value)
	{
		final double imaginary = this.realValue * value.imaginaryValue + this.imaginaryValue * value.realValue;
		final double real = this.realValue * value.realValue - this.imaginaryValue * value.imaginaryValue;
		return new ComplexNumber(real, imaginary);
	}

	public final ComplexNumber multiply(double value)
	{
		return new ComplexNumber(value * realValue, value * imaginaryValue);
	}
	
	public final ComplexNumber divide(ComplexNumber value)
	{
		return this.multiply(value.reciprocal());
	}

	public final ComplexNumber divide(double value)
	{
		return this.divide(new ComplexNumber(value, 0.0));
	}

	public final ComplexNumber exp()
	{
		return new ComplexNumber(Math.exp(realValue) * Math.cos(imaginaryValue), Math.exp(realValue) * Math.sin(imaginaryValue));
	}

	public final ComplexNumber log()
	{
		return new ComplexNumber(Math.log(this.abs()), Math.atan2(this.imaginaryValue, this.realValue));
	}

	public final ComplexNumber pow(ComplexNumber exponent)
	{
		if(exponent == null)
			throw new IllegalArgumentException("exponent can not be null");

		return this.log().multiply(exponent).exp();
	}

	public final ComplexNumber pow(double exponent)
	{
		return this.log().multiply(exponent).exp();
	}

	public List<ComplexNumber> root(int n)
	{
		if (n <= 0)
			throw new IllegalArgumentException("n must be greater than 0");

		List<ComplexNumber> result = new ArrayList<ComplexNumber>();

		double inner = this.phase()/n;
		for (int nIndex = 0; nIndex < n ; nIndex++)
		{
			result.add(new ComplexNumber(Math.cos(inner) * Math.pow(this.abs(), 1.0 / n), Math.sin(inner) * Math.pow(this.abs(), 1.0 / n)));
			inner += 2 * Math.PI / n;
		}

		return Collections.unmodifiableList(result);
	}


	public final ComplexNumber sqrt()
	{
		//The square-root of the complex number (a + i b) is
		//sqrt(a + i b) = +/- (sqrt(r + a) + i sqrt(r - a) sign(b)) sqrt(2) / 2,
		//where r = sqrt(a^2 + b^2).
		double r = Math.sqrt((this.realValue*this.realValue) + (this.imaginaryValue*this.imaginaryValue));
		ComplexNumber intermediate = new ComplexNumber(Math.sqrt(r + this.realValue), Math.sqrt(r + this.realValue) * Math.signum(this.imaginaryValue));
		return intermediate.multiply(Math.sqrt(2.0)).divide(2.0);
	}

	public ComplexNumber sqrt1z()
	{
		return (new ComplexNumber(1.0, 0.0)).subtract(this.multiply(this)).sqrt();
	}


	public final ComplexNumber sin()
	{
		return new ComplexNumber(Math.sin(realValue) * Math.cosh(imaginaryValue), Math.cos(realValue) * Math.sinh(imaginaryValue));
	}

	public final ComplexNumber asin()
	{
		return sqrt1z().add(this.multiply(ComplexNumber.I)).log().multiply(ComplexNumber.I.negate());
	}

	public final ComplexNumber sinh()
	{
		return new ComplexNumber(Math.sinh(this.realValue) * Math.cos(this.imaginaryValue), Math.cosh(this.realValue) * Math.sin(this.imaginaryValue));
	}

	public final ComplexNumber cos()
	{
		return new ComplexNumber(Math.cos(realValue) * Math.cosh(imaginaryValue), -Math.sin(realValue) * Math.sinh(imaginaryValue));
	}

	public final ComplexNumber acos()
	{
		return this.add(this.sqrt1z().multiply(ComplexNumber.I)).log().multiply(ComplexNumber.I.negate());
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
		double denominator = Math.cosh(2.0 * this.realValue) + Math.cos(2.0 * this.imaginaryValue);
		return new ComplexNumber(Math.sinh(2.0 * this.realValue) / denominator, Math.sin(2.0 * this.imaginaryValue) / denominator);
	}

	public final ComplexNumber negate()
	{
		return new ComplexNumber(-this.realValue, -this.imaginaryValue);
	}

	public final ComplexNumber reciprocal()
	{
		final double scale = (realValue * realValue) + (imaginaryValue * imaginaryValue);
		return new ComplexNumber(realValue / scale, -imaginaryValue / scale);
	}

	public final ComplexNumber conjugate()
	{
		return new ComplexNumber(realValue, -imaginaryValue);
	}

	public final double getRealValue()
	{
		return realValue;
	}

	public final double getImaginaryValue()
	{
		return imaginaryValue;
	}
	
	@Override
	public int hashCode()
	{
		int imaginaryHash = Double.valueOf(this.getImaginaryValue()).hashCode();
		int realHash = Double.valueOf(this.getRealValue()).hashCode();
		return (imaginaryHash * realHash) + realHash;
	}

	@Override
	public boolean equals(Object compareObject)
	{
		if(!(compareObject instanceof ComplexNumber))
			return false;

		ComplexNumber compareComplex = (ComplexNumber) compareObject;
		if(compareComplex.getRealValue() != this.getRealValue())
			return false;
		if(compareComplex.getImaginaryValue() != this.getImaginaryValue())
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		if( imaginaryValue == 0 )
			return realValue + "0";
		if( realValue == 0 )
			return imaginaryValue + "i";
		if( imaginaryValue <  0 )
			return realValue + " - " + -imaginaryValue + "i";
		return realValue + " + " + imaginaryValue + "i";
	}

	public static ComplexNumber sum(ComplexNumber... values)
	{
		ComplexNumber complexSum = ComplexNumber.ZERO;
		for(ComplexNumber value : values)
			complexSum = complexSum.add(value);
		return complexSum;
	}

	public static ComplexNumber multiply(ComplexNumber... values)
	{
		ComplexNumber complexProduct = new ComplexNumber(1.0, 0.0);
		for(ComplexNumber value : values)
			complexProduct = complexProduct.multiply(value);
		return complexProduct;
	}
}
