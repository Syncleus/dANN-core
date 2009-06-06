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
package com.syncleus.dann.activation;

/**
 * An implementation of an activation function using a hyperbolic tangent
 * function.
 *
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @version 0.1
 */
public class HyperbolicTangentActivationFunction implements ActivationFunction
{
	/**
	 * The hyperbolic tangent activation function.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param activity the neuron's current activity.
	 * @return The result of the hyperbolic tangent activation function bound
	 * between -1 and 1.
	 * @since 0.1
	 */
    public double activate(double activity)
    {
        return Math.tanh(activity);
    }

	/**
	 * The derivative of the hyperbolic tangent activation function.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param activity The neuron's current activity.
	 * @return The result of the derivative of the hyperbolic tangent activation
	 * function.
	 * @since 0.1
	 */
    public double activateDerivative(double activity)
    {
        return 1.0 - Math.pow(this.activate(activity), 2.0);
    }
}
