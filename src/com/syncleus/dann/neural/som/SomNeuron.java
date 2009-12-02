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
import com.syncleus.dann.neural.activation.SqrtActivationFunction;

/**
 * A Som Nearon will calculate its euclidean distance to the input vector as
 * its output.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public class SomNeuron extends AbstractNeuron implements OutputNeuron
{
	private final static SqrtActivationFunction ACTIVATION_FUNCTION = new SqrtActivationFunction();

	/**
	 * Creates a default SomNeuron.
	 *
	 * @since 2.0
	 */
	public SomNeuron(Brain brain)
	{
		super(brain, ACTIVATION_FUNCTION);
	}

	/**
	 * Trains the neuron to be closer to the input vector according to the
	 * specefied parameters.
	 *
	 * @since 2.0
	 */
	public void train(double learningRate, double neighborhoodAdjustment)
	{
		for(Synapse source : this.getBrain().getInEdges(this))
			source.setWeight( source.getWeight() + (learningRate * neighborhoodAdjustment * (source.getInput() - source.getWeight())) );
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
        activity = 0;
        for (Synapse currentSynapse : this.getBrain().getInEdges(this))
            activity += Math.pow(currentSynapse.getInput() - currentSynapse.getWeight(), 2.0);
        //Add the bias to the activity
        super.activity = activity;

        //calculate the activity function and set the result as the output
        this.setOutput(this.activate());
    }

	/**
	 * Obtains the current output for this neuron.
	 *
	 * @since 2.0
	 * @return The current output of the neuron.
	 */
	@Override
	public double getOutput()
	{
		return super.getOutput();
	}
}
