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
package com.syncleus.dann.neural.som;

import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.activation.*;

/**
 * A Som Nearon will calculate its euclidean distance to the input vector as its
 * output.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public class SimpleSomNeuron extends AbstractNeuron implements SomNeuron
{
	private static final long serialVersionUID = -4237625154747173055L;
	private final static ActivationFunction ACTIVATION_FUNCTION = new SqrtActivationFunction();
	private final ActivationFunction activationFunction;
	private double output = 0.0;

	/**
	 * Creates a default SimpleSomNeuron.
	 *
	 * @since 2.0
	 */
	public SimpleSomNeuron(final Brain brain)
	{
		super(brain);
		this.activationFunction = ACTIVATION_FUNCTION;
	}

	/**
	 * Trains the neuron to be closer to the input vector according to the
	 * specefied parameters.
	 *
	 * @since 2.0
	 */
	public void train(final double learningRate, final double neighborhoodAdjustment)
	{
		for(final Synapse source : this.getBrain().getInEdges(this))
			source.setWeight(source.getWeight() + (learningRate * neighborhoodAdjustment * (source.getInput() - source.getWeight())));
	}

	/**
	 * Propogates all the inputs to determine to caculate the output.
	 *
	 * @since 2.0
	 */
	@Override
	public void propagate()
	{
		//calculate the current input activity
		double activity = 0.0;
		for(final Synapse currentSynapse : this.getBrain().getInEdges(this))
			activity += Math.pow(currentSynapse.getInput() - currentSynapse.getWeight(), 2.0);
		//calculate the activity function and set the result as the output
		this.output = this.activationFunction.activate(activity);
		for(final Synapse current : this.getBrain().getTraversableEdges(this))
			current.setInput(this.output);
	}

	/**
	 * Obtains the current output for this neuron.
	 *
	 * @return The current output of the neuron.
	 * @since 2.0
	 */
	@Override
	public double getOutput()
	{
		return this.output;
	}
}
