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


import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.activation.*;

/**
 * This is a special type of neuron that provides the output.
 *
 *
 * @author Syncleus, Inc.
 * @since 1.0
 * @see com.syncleus.dann.neural.backprop.InputBackpropNeuron
 */
public class OutputBackpropNeuron extends BackpropNeuron implements OutputNeuron<AbstractNeuron, BackpropNeuron>
{
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
    public OutputBackpropNeuron()
    {
        super();
    }
    
    /**
     * Creates a new instance of OutputBackpropNeuron using the specified
	 * activation function.
	 *
     *
	 * @param activationFunction The activation function to use.
     * @since 1.0
     */
    public OutputBackpropNeuron(ActivationFunction activationFunction)
    {
        super(activationFunction);
    }

	/**
	 * Creates a new instance of this class using the specified learning rate.
	 *
	 *
	 * @param learningRate The learning rate for this neuron.
	 * @since 1.0
	 */
	public OutputBackpropNeuron(double learningRate)
	{
		super(learningRate);
	}

	/**
	 * Creates a new instance of this class with the specified activation
	 * function and learning rate.
	 *
	 *
	 * @param activationFunction The activation used by this neuron.
	 * @param learningRate The learning rate for this neuron.
	 * @since 1.0
	 */
	public OutputBackpropNeuron(ActivationFunction activationFunction, double learningRate)
	{
		super(activationFunction, learningRate);
	}



    /**
     * This method sets the expected output for this neuron to learn from.
	 *
     *
     * @since 1.0
     * @param trainingToSet sets the current desired output.
     */
    public void setDesired(double trainingToSet)
    {
        this.desired = trainingToSet;
    }



    /**
     * Calculates the Delta Train based on all the destination synapses
	 *
     *
     * @since 1.0
     * @see com.syncleus.dann.neural.backprop.BackpropNeuron#backPropagate
     */
	@Override
    protected void calculateDeltaTrain()
    {
        this.deltaTrain = 0;
        for (Synapse currentSynapse : super.destinations)
			this.deltaTrain += (currentSynapse.getWeight() * this.deltaTrainDestinations.get(currentSynapse).doubleValue());

        this.deltaTrain += (this.desired - this.getOutput());

        this.deltaTrain *= super.activateDerivitive();
    }

	/**
	 * Connects this Neuron to the specified Neuron.
	 *
	 *
	 * @param outUnit The Neuron to connect to.
	 * @throws com.syncleus.dann.InvalidConnectionTypeDannException The
	 * specified neuron to connect to is not valid.
	 * @return newly created Synapse.
	 * @since 1.0
	 */
	@Override
    public Synapse connectTo(BackpropNeuron outUnit) throws InvalidConnectionTypeDannException
    {
        throw new InvalidConnectionTypeDannException("Can not connect from a OutputNeuron");
    }

	/**
	 * Obtains the current output for this neuron.
	 *
	 *
	 * @return The current output of the neuron.
	 */
	@Override
	public double getOutput()
	{
		return super.getOutput();
	}
}
