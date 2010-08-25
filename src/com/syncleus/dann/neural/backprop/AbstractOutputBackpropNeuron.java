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
package com.syncleus.dann.neural.backprop;

import com.syncleus.dann.neural.Brain;
import com.syncleus.dann.neural.Synapse;
import com.syncleus.dann.neural.activation.ActivationFunction;

public abstract class AbstractOutputBackpropNeuron extends AbstractBackpropNeuron implements OutputBackpropNeuron
{
	private static final long serialVersionUID = -4643866124019076672L;
	/**
	 * holds the value for the current training set.
	 *
	 * @since 1.0
	 */
	protected double desired;

	/**
	 * Creates a new instance of OutputBackpropNeuron
	 *
	 * @since 1.0
	 */
	protected AbstractOutputBackpropNeuron(final Brain brain)
	{
		super(brain);
	}

	/**
	 * Creates a new instance of OutputBackpropNeuron using the specified
	 * activation function.
	 *
	 * @param activationFunction The activation function to use.
	 * @since 1.0
	 */
	protected AbstractOutputBackpropNeuron(final Brain brain, final ActivationFunction activationFunction)
	{
		super(brain, activationFunction);
	}

	/**
	 * Creates a new instance of this class using the specified learning rate.
	 *
	 * @param learningRate The learning rate for this neuron.
	 * @since 1.0
	 */
	protected AbstractOutputBackpropNeuron(final Brain brain, final double learningRate)
	{
		super(brain, learningRate);
	}

	/**
	 * Creates a new instance of this class with the specified activation function
	 * and learning rate.
	 *
	 * @param activationFunction The activation used by this neuron.
	 * @param learningRate The learning rate for this neuron.
	 * @since 1.0
	 */
	protected AbstractOutputBackpropNeuron(final Brain brain, final ActivationFunction activationFunction, final double learningRate)
	{
		super(brain, activationFunction, learningRate);
	}

	/**
	 * This method sets the expected output for this neuron to learn from.
	 *
	 * @param trainingToSet sets the current desired output.
	 * @since 1.0
	 */
	@Override
	public void setDesired(final double trainingToSet)
	{
		this.desired = trainingToSet;
	}

	/**
	 * Calculates the Delta Train based on all the destination synapses
	 *
	 * @see com.syncleus.dann.neural.backprop.SimpleBackpropNeuron#backPropagate
	 * @since 1.0
	 */
	@Override
	protected void calculateDeltaTrain()
	{
		this.deltaTrain = 0;
		// TODO fix this, bad typing
//		for(final Synapse currentSynapse : super.getBrain().getTraversableEdges(this))
//			this.deltaTrain += (currentSynapse.getWeight() * this.deltaTrainDestinations.get(currentSynapse));
		for(final Object currentSynapse : super.getBrain().getTraversableEdges(this))
			this.deltaTrain += (((Synapse)currentSynapse).getWeight() * this.deltaTrainDestinations.get(currentSynapse));
		this.deltaTrain += (this.desired - this.getOutput());
		this.deltaTrain *= super.activateDerivitive();
	}

	/**
	 * Obtains the current output for this neuron.
	 *
	 * @return The current output of the neuron.
	 */
	@Override
	public double getOutput()
	{
		return super.getOutput();
	}
}
