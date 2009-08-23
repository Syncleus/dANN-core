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

public class ComplexNumber
{
	public final static ComplexNumber ZERO = new ComplexNumber(0, 0);
	
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

    public final ComplexNumber subtract(ComplexNumber value)
	{
        return new ComplexNumber(this.realValue - value.realValue, this.imaginaryValue - value.imaginaryValue);
    }

    public final ComplexNumber multiply(ComplexNumber value)
	{
		final double imaginary = this.realValue * value.imaginaryValue + this.imaginaryValue * value.realValue;
        final double real = this.realValue * value.realValue - this.imaginaryValue * value.imaginaryValue;
        return new ComplexNumber(real, imaginary);
    }

    //scalar multiplication
    public final ComplexNumber multiply(double value)
	{
        return new ComplexNumber(value * realValue, value * imaginaryValue);
    }
	
    public final ComplexNumber divide(ComplexNumber value)
	{
        ComplexNumber a = this;
        return a.multiply(value.reciprocal());
    }

    public final ComplexNumber exp()
	{
        return new ComplexNumber(Math.exp(realValue) * Math.cos(imaginaryValue), Math.exp(realValue) * Math.sin(imaginaryValue));
    }

    public final ComplexNumber sin()
	{
        return new ComplexNumber(Math.sin(realValue) * Math.cosh(imaginaryValue), Math.cos(realValue) * Math.sinh(imaginaryValue));
    }

    public final ComplexNumber cos()
	{
        return new ComplexNumber(Math.cos(realValue) * Math.cosh(imaginaryValue), -Math.sin(realValue) * Math.sinh(imaginaryValue));
    }

    public final ComplexNumber tan()
	{
        return sin().divide(cos());
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
}
