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

public class TestActivationValues
{
	private static Random random = new Random();

	private static GausianActivationFunction gausianActivationFunction = new GausianActivationFunction();
	private static HyperbolicSecantActivationFunction hyperbolicSecantActivationFunction = new HyperbolicSecantActivationFunction();
	private static HyperbolicTangentActivationFunction hyperbolicTangentActivationFunction = new HyperbolicTangentActivationFunction();
	private static SineActivationFunction sineActivationFunction = new SineActivationFunction();

	private ArrayList<ActivationFunction> activationFunctions = new ArrayList<ActivationFunction>();

	private static final double UPPER_TEST_VALUE = 1000000000.0;
	private static final double UPPER_CUTOFF_VALUE = 100.0;
	private static final double LOWER_TEST_VALUE = -1000000000.0;
	private static final double LOWER_CUTOFF_VALUE = -100.0;
	private static final double TEST_INCREMENT = 10.0;
	private static final int RANDOM_TEST_ITERATIONS = 10000;
	private static final double RANDOM_TEST_RANGE = 1000.0;


	public TestActivationValues()
	{
		activationFunctions.add(this.gausianActivationFunction);
		activationFunctions.add(this.hyperbolicSecantActivationFunction);
		activationFunctions.add(this.hyperbolicTangentActivationFunction);
		activationFunctions.add(this.sineActivationFunction);
	}

	@Test
	public void testBounds()
	{
		for(ActivationFunction currentActivationFunction : this.activationFunctions)
		{
			double currentIn = UPPER_TEST_VALUE;
			while(currentIn >= UPPER_CUTOFF_VALUE)
			{
				currentActivationFunction.activateDerivative(currentIn);
				double result = currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= 1.0)&&(result >= -1.0));

				currentIn = currentIn/TEST_INCREMENT;
			}
			while(currentIn > 0.0)
			{
				currentActivationFunction.activateDerivative(currentIn);
				double result = currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= 1.0)&&(result >= -1.0));

				currentIn--;
			}

			currentIn = LOWER_TEST_VALUE;
			while(currentIn <= LOWER_CUTOFF_VALUE)
			{
				currentActivationFunction.activateDerivative(currentIn);
				double result = currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= 1.0)&&(result >= -1.0));

				currentIn = currentIn/TEST_INCREMENT;
			}
			while(currentIn <= 0.0)
			{
				currentActivationFunction.activateDerivative(currentIn);
				double result = currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= 1.0)&&(result >= -1.0));

				currentIn++;
			}

			for(int count = 0; count < RANDOM_TEST_ITERATIONS; count++)
			{
				currentIn = ((random.nextDouble()*2.0)-1.0) * RANDOM_TEST_RANGE;
				currentActivationFunction.activateDerivative(currentIn);
				double result = currentActivationFunction.activate(currentIn);
				Assert.assertTrue("Transfer out of bounds. In: " + currentIn + ", result: " + result, (result <= 1.0)&&(result >= -1.0));
			}
		}
	}
}
