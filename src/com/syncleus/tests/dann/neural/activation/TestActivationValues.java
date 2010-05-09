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
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class TestActivationValues
{
	private static final Logger LOGGER = Logger.getLogger(TestActivationValues.class);
	private static final ActivationFunction GAUSIAN_ACTIVATION_FUNCTION = new GausianActivationFunction();
	private static final double[] GAUSIAN_ACTIVITY = {-10000.0,
			-100.0,
			-10.0,
			-1.0,
			-0.1,
			0.0,
			0.1,
			1.0,
			10.0,
			100.0,
			10000.0};
	private static final double[] GAUSIAN_TRANSFERED = {scientific(6.451709693, -43429449),
			scientific(1.135483865, -4343),
			scientific(3.720075976, -44),
			0.3678794412,
			0.9900498337,
			1.0,
			0.9900498337,
			0.3678794412,
			scientific(3.720075976, -44),
			scientific(1.135483865, -4343),
			scientific(6.451709693, -43429449)};
	private static final ActivationFunction IDENTITY_ACTIVATION_FUNCTION = new IdentityActivationFunction();
	private static final double[] IDENTITY_ACTIVITY = {-10000.0, -5000.0, -1000.0, -500.0, -100.0,
			-50.0, -10.0, -5.0, -1.0, -0.5, -0.1, -0.05,
			0.0, 0.05, 0.1, 0.5, 1.0, 5.0, 10.0, 50.0,
			100.0, 500.0, 1000.0, 5000.0, 10000.0};
	private static final double[] IDENTITY_TRANSFERED = IDENTITY_ACTIVITY;
	private static final ActivationFunction HYPERBOLIC_SECANT_ACTIVATION_FUNCTION = new HyperbolicSecantActivationFunction();
	private static final double[] HYPERBOLIC_SECANT_ACTIVITY = {-10000.0,
			-100.0,
			-10.0,
			-1.0,
			-0.1,
			0.0,
			0.1,
			1.0,
			10.0,
			100.0,
			10000.0};
	private static final double[] HYPERBOLIC_SECANT_TRANSFERED = {scientific(2.270967731, -4343),
			scientific(7.440151952, -44),
			0.00009079985934,
			0.6480542737,
			0.9950207489,
			1.0,
			0.9950207489,
			0.6480542737,
			0.00009079985934,
			scientific(7.440151952, -44),
			scientific(2.270967731, -4343)};
	private static final ActivationFunction HYPERBOLIC_TANGENT_ACTIVATION_FUNCTION = new HyperbolicTangentActivationFunction();
	private static final double[] HYPERBOLIC_TANGENT_ACTIVITY = {-10000.0,
			-100.0,
			-10.0,
			-1.0,
			-0.1,
			0.0,
			0.1,
			1.0,
			10.0,
			100.0,
			10000.0};
	private static final double[] HYPERBOLIC_TANGENT_TRANSFERED = {-1.0,
			-1.0,
			-0.9999999959,
			-0.7615941560,
			-0.09966799462,
			0.0,
			0.09966799462,
			0.7615941560,
			0.9999999959,
			1.0,
			1.0};
	private static final ActivationFunction SINE_ACTIVATION_FUNCTION = new SineActivationFunction();
	private static final double[] SINE_ACTIVITY = {-10000.0,
			-100.0,
			-10.0,
			-1.0,
			-0.1,
			0.0,
			0.1,
			1.0,
			10.0,
			100.0,
			10000.0};
	private static final double[] SINE_TRANSFERED = {0.3056143889,
			0.5063656411,
			0.5440211109,
			-0.8414709848,
			-0.09983341665,
			0.0,
			0.09983341665,
			0.8414709848,
			-0.5440211109,
			-0.5063656411,
			-0.3056143889};

	@Test
	public void testActivations()
	{
		Assert.assertTrue("Gausian failed!", checkFunction(GAUSIAN_ACTIVATION_FUNCTION, GAUSIAN_ACTIVITY, GAUSIAN_TRANSFERED));
		Assert.assertTrue("Identity failed!", checkFunction(IDENTITY_ACTIVATION_FUNCTION, IDENTITY_ACTIVITY, IDENTITY_TRANSFERED));
		Assert.assertTrue("SecH failed!", checkFunction(HYPERBOLIC_SECANT_ACTIVATION_FUNCTION, HYPERBOLIC_SECANT_ACTIVITY, HYPERBOLIC_SECANT_TRANSFERED));
		Assert.assertTrue("TanH failed!", checkFunction(HYPERBOLIC_TANGENT_ACTIVATION_FUNCTION, HYPERBOLIC_TANGENT_ACTIVITY, HYPERBOLIC_TANGENT_TRANSFERED));
		Assert.assertTrue("Sine failed!", checkFunction(SINE_ACTIVATION_FUNCTION, SINE_ACTIVITY, SINE_TRANSFERED));
	}

	private static boolean checkFunction(final ActivationFunction function, final double[] activities, final double[] transfered)
	{
		for(int testIndex = 0; testIndex < activities.length; testIndex++)
		{
			final double result = function.activate(activities[testIndex]);
			if (!checkResult(result, transfered[testIndex]))
			{
				LOGGER.error("Activation function " + function + " failed. Expected: " + transfered[testIndex] + ", Received: " + result);
				return false;
			}
		}
		return true;
	}

	private static boolean checkResult(final double firstValue, final double secondValue)
	{
		return (Math.abs(firstValue - secondValue) < 0.0000001);
	}

	private static double scientific(final double value, final double exponent)
	{
		return value * Math.pow(10.0, exponent);
	}
}
