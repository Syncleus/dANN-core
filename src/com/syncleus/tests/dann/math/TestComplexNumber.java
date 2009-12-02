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
package com.syncleus.tests.dann.math;

import com.syncleus.dann.math.ComplexNumber;
import org.junit.*;

public class TestComplexNumber
{
	@Test
	public void testComplexMath()
	{
        final ComplexNumber valueA = new ComplexNumber(-8.0, 2.0);
        final ComplexNumber valueB = new ComplexNumber(7.0, 5.0);
		ComplexNumber result;
		ComplexNumber expected;

		result = valueA.add(valueB);
		expected = new ComplexNumber(-1.0, 7.0);
		Assert.assertTrue("addition failed: " + valueA + " + " + valueB + " = " + result, result.equals(expected));

		result = valueA.subtract(valueB);
		expected = new ComplexNumber(-15.0, -3.0);
		Assert.assertTrue("subtraction failed: " + valueA + " - " + valueB + " = " + result, result.equals(expected));

		result = valueA.multiply(valueB);
		expected = new ComplexNumber(-66.0, -26.0);
		Assert.assertTrue("multiplication failed: " + valueA + " * " + valueB + " = " + result, result.equals(expected));

		result = valueA.multiply(10.0);
		expected = new ComplexNumber(-80.0, 20.0);
		Assert.assertTrue("multiplication failed: " + valueA + " * " + 10.0 + " = " + result, result.equals(expected));

		result = valueA.divide(valueB);
		expected = new ComplexNumber(-0.6216216216216217, 0.7297297297297298);
		Assert.assertTrue("division failed: " + valueA + " / " + valueB + " = " + result, result.subtract(expected).absScalar() <0.0001);

		result = valueA.conjugate();
		expected = new ComplexNumber(-8.0, -2.0);
		Assert.assertTrue("conjugation failed: conjugate(" + valueA + ") = " + result, result.equals(expected));

		Assert.assertTrue("absolute failed: abs(" + valueA + ") = " + valueA.absScalar(), (valueA.absScalar() - 8.246211251235321) < 0.0001);

		result = valueA.tan();
		expected = new ComplexNumber(0.010925884335752506, 1.0356479469632376);
		Assert.assertTrue("conjugation failed: conjugate(" + valueA + ") = " + result, result.subtract(expected).absScalar() <0.0001);
	}
}
