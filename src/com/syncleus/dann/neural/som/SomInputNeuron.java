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
package com.syncleus.dann.neural.som;

import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.activation.IdentityActivationFunction;

/**
 * An input neuron for a SOM network. It essentialy just propgates the input
 * unchanged to the next layer.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public class SomInputNeuron extends AbstractNeuron implements InputNeuron
{
	private double input;
	private static final IdentityActivationFunction ACTIVATION_FUNCTION = new IdentityActivationFunction();

	/**
	 * Creates a default SomInputNeuron using an IdentityActivationFunction
	 *
	 * @since 2.0
	 */
	public SomInputNeuron(Brain brain)
	{
		super(brain, ACTIVATION_FUNCTION);
	}

	/**
	 * Propogates the input to all connected SomNeurons.
	 * 
	 * @since 2.0
	 */
	@Override
	public void propagate()
	{
		this.activity = this.input;
		this.setOutput(this.activity);
	}

	/**
	 * Sets the current input for this neuron.
	 *
	 * @since 2.0
	 * @param inputToSet The new input value you want to set.
	 */
	public void setInput(double inputToSet)
	{
		this.input = inputToSet;
	}

	/**
	 * Gets the current input.
	 *
	 * @return the current input.
	 * @since 2.0
	 */
	public double getInput()
	{
		return this.input;
	}
}
