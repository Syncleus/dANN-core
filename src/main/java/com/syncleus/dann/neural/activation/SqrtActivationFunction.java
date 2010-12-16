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
package com.syncleus.dann.neural.activation;

/**
 * An activiation function using a square root transfer function.
 *
 * @author Syncleus. Inc.
 * @since 2.0
 */
public class SqrtActivationFunction implements ActivationFunction
{
	private static final long serialVersionUID = -7384243647996712000L;
	private static final double UPPER_LIMIT = Double.MAX_VALUE;
	private static final double LOWER_LIMIT = 0.0;

	/**
	 * The activation function.
	 *
	 * @param activity the neuron's current activity.
	 * @return The result of the activation function. Usually a bound value between
	 *         1 and -1 or 1 and 0. However this bound range is not required.
	 * @since 2.0
	 */
	public double activate(final double activity)
	{
		return Math.sqrt(activity);
	}

	/**
	 * The derivative of the activation function.
	 *
	 * @param activity The neuron's current activity.
	 * @return The result of the derivative of the activation function.
	 * @since 2.0
	 */
	public double activateDerivative(final double activity)
	{
		return 1.0 / (2.0 * Math.sqrt(activity));
	}

	public boolean isBound()
	{
		return false;
	}

	public double getUpperLimit()
	{
		return UPPER_LIMIT;
	}

	public double getLowerLimit()
	{
		return LOWER_LIMIT;
	}
}
