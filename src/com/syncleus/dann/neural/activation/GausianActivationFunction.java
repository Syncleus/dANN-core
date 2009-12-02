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
 * An implementation of an activation function using a gausian function.
 *
 *
 * @author Syncleus, Inc.
 * @since 1.0
 *
 */
public class GausianActivationFunction implements ActivationFunction
{
	/**
	 * The gausian activation function.
	 *
	 *
	 * @param activity the neuron's current activity.
	 * @return The result of the gausian activation function bound between 0 and
	 * 1.
	 * @since 1.0
	 */
    public double activate(double activity)
    {
        return Math.pow(Math.E, (-1.0 * Math.pow(activity,2) ));
    }

	/**
	 * The derivative of the gausian activation function.
	 *
	 *
	 * @param activity The neuron's current activity.
	 * @return The result of the derivative of the gausian activation function.
	 * @since 1.0
	 */
    public double activateDerivative(double activity)
    {
        return (-2.0 * Math.log10(Math.E) * activity) / Math.pow(Math.E, Math.pow(activity,2));
    }
}
