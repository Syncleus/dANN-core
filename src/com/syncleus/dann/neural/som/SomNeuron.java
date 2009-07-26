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
import com.syncleus.dann.neural.activation.SqrtActivationFunction;

/**
 * A Som Nearon will calculate its euclidean distance to the input vector as
 * its output.
 *
 * @author Syncleus, Inc.
 * @since 2.0
 */
public class SomNeuron extends NeuronImpl<NeuronImpl, SomNeuron> implements OutputNeuron<NeuronImpl, SomNeuron>
{
	private final static SqrtActivationFunction ACTIVATION_FUNCTION = new SqrtActivationFunction();

	/**
	 * Creates a default SomNeuron.
	 *
	 * @since 2.0
	 */
	public SomNeuron()
	{
		super(ACTIVATION_FUNCTION);
	}

	/**
	 * Trains the neuron to be closer to the input vector according to the
	 * specefied parameters.
	 *
	 * @since 2.0
	 */
	void train(double learningRate, double neighborhoodAdjustment)
	{
		for(Synapse source : this.getSources())
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
        for (Synapse currentSynapse : this.getSources())
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

	/**
	 * This should never be called, This class can not connect outward, only
	 * receive. Any call to this method will always throw an
	 * InvalidConnectionTypeDannException.
	 *
	 * @param outUnit This is ignored.
	 * @throws com.syncleus.dann.InvalidConnectionTypeDannException Always
	 * thrown
	 * @since 2.0
	 */
	@Override
	public Synapse connectTo(SomNeuron outUnit) throws InvalidConnectionTypeDannException
	{
		throw new InvalidConnectionTypeDannException("SOM networks' SomNeuron can not connect to any other neurons.");
	}
}
