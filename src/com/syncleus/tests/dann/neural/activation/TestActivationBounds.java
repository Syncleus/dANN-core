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
package com.syncleus.tests.dann.neural.activation;

import com.syncleus.dann.neural.activation.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.Random;

public class TestActivationBounds
{
	private static final Random random = new Random();
	private final static GausianActivationFunction GAUSIAN_ACTIVATION_FUNCTION = new GausianActivationFunction();
	private final static HyperbolicSecantActivationFunction HYPERBOLIC_SECANT_ACTIVATION_FUNCTION = new HyperbolicSecantActivationFunction();
	private final static HyperbolicTangentActivationFunction HYPERBOLIC_TANGENT_ACTIVATION_FUNCTION = new HyperbolicTangentActivationFunction();
	private final static SineActivationFunction SINE_ACTIVATION_FUNCTION = new SineActivationFunction();
	private final ArrayList<ActivationFunction> activationFunctions = new ArrayList<ActivationFunction>();
	private static final double UPPER_TEST_VALUE = 1000000000.0;
	private static final double UPPER_CUTOFF_VALUE = 100.0;
	private static final double LOWER_TEST_VALUE = -1000000000.0;
	private static final double LOWER_CUTOFF_VALUE = -100.0;
	private static final double TEST_INCREMENT = 10.0;
	private static final int RANDOM_TEST_ITERATIONS = 10000;
	private static final double RANDOM_TEST_RANGE = 1000.0;

	public TestActivationBounds()
	{
		activationFunctions.add(GAUSIAN_ACTIVATION_FUNCTION);
		activationFunctions.add(HYPERBOLIC_SECANT_ACTIVATION_FUNCTION);
		activationFunctions.add(HYPERBOLIC_TANGENT_ACTIVATION_FUNCTION);
		activationFunctions.add(SINE_ACTIVATION_FUNCTION);
	}

	@Test
	public void testBounds()
	{
		for(final ActivationFunction currentActivationFunction : this.activationFunctions)
		{
			double currentIn = UPPER_TEST_VALUE;
			while (currentIn >= UPPER_CUTOFF_VALUE)
			{
				currentActivationFunction.activateDerivative(currentIn);
				final double result = currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= currentActivationFunction.getUpperLimit()) && (result >= currentActivationFunction.getLowerLimit()));
				currentIn = currentIn / TEST_INCREMENT;
			}
			while (currentIn > 0.0)
			{
				currentActivationFunction.activateDerivative(currentIn);
				final double result = currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= currentActivationFunction.getUpperLimit()) && (result >= currentActivationFunction.getLowerLimit()));
				currentIn--;
			}
			currentIn = LOWER_TEST_VALUE;
			while (currentIn <= LOWER_CUTOFF_VALUE)
			{
				currentActivationFunction.activateDerivative(currentIn);
				final double result = currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= currentActivationFunction.getUpperLimit()) && (result >= currentActivationFunction.getLowerLimit()));
				currentIn = currentIn / TEST_INCREMENT;
			}
			while (currentIn <= 0.0)
			{
				currentActivationFunction.activateDerivative(currentIn);
				final double result = currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= currentActivationFunction.getUpperLimit()) && (result >= currentActivationFunction.getLowerLimit()));
				currentIn++;
			}
			for(int count = 0; count < RANDOM_TEST_ITERATIONS; count++)
			{
				currentIn = ((random.nextDouble() * 2.0) - 1.0) * RANDOM_TEST_RANGE;
				currentActivationFunction.activateDerivative(currentIn);
				final double result = currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= currentActivationFunction.getUpperLimit()) && (result >= currentActivationFunction.getLowerLimit()));
			}
		}
	}
}
