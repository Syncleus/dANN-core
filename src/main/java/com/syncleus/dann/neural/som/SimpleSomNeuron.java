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

import com.syncleus.dann.neural.AbstractNeuron;
import com.syncleus.dann.neural.Brain;
import com.syncleus.dann.neural.Neuron;
import com.syncleus.dann.neural.Synapse;
import com.syncleus.dann.neural.activation.ActivationFunction;
import com.syncleus.dann.neural.activation.SqrtActivationFunction;

/**
 * A SOM neuron will calculate its euclidean distance to the input vector as its
 * output.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public class SimpleSomNeuron extends AbstractNeuron implements SomOutputNeuron
{
	private static final long serialVersionUID = -4237625154747173055L;
	private static final ActivationFunction ACTIVATION_FUNCTION = new SqrtActivationFunction();
	private final ActivationFunction activationFunction;
	private double output = 0.0;

	/**
	 * Creates a default SimpleSomNeuron.
	 *
	 * @param brain The brain which contains this neuron.
	 * @since 2.0
	 */
	public SimpleSomNeuron(final Brain brain)
	{
		super(brain);
		this.activationFunction = ACTIVATION_FUNCTION;
	}

	/**
	 * Trains the neuron to be closer to the input vector according to the
	 * specified parameters.
	 *
	 * @since 2.0
	 */
	@Override
	public void train(final double learningRate, final double neighborhoodAdjustment)
	{
		for (final Synapse<Neuron> source : getBrain().getInEdges(this))
		{
			source.setWeight(source.getWeight() + (learningRate * neighborhoodAdjustment * (source.getInput() - source.getWeight())));
		}
	}

	/**
	 * Propagates all the inputs to determine to calculate the output.
	 *
	 * @since 2.0
	 */
	@Override
	public void tick()
	{
		// calculate the current input activity
		double activity = 0.0;
		for (final Synapse<Neuron> currentSynapse : getBrain().getInEdges(this))
		{
			activity += Math.pow(currentSynapse.getInput() - currentSynapse.getWeight(), 2.0);
		}

		// calculate the activity function and set the result as the output
		output = activationFunction.activate(activity);
		for (final Synapse<Neuron> current : getBrain().getTraversableEdges(this))
		{
			current.setInput(output);
		}
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
