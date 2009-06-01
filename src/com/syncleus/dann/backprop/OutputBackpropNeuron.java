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
package com.syncleus.dann.backprop;

import com.syncleus.dann.*;
import com.syncleus.dann.activation.*;

/**
 * This is a special type of neuron that provides the output.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 * @see com.syncleus.dann.InputNeuron
 */
public class OutputBackpropNeuron extends BackpropNeuron implements OutputNeuron<NeuronImpl, BackpropNeuron>
{
    /**
     * holds the value for the current training set.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     */
    protected double desired;



    /**
     * Creates a new instance of OutputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public OutputBackpropNeuron()
    {
        super();
    }
    
    /**
     * Creates a new instance of OutputNeuron<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param ownedDNAToSet This dna class will determine the various properties
     * 	of the layer.
     */
    public OutputBackpropNeuron(ActivationFunction activationFunction)
    {
        super(activationFunction);
    }

	public OutputBackpropNeuron(double learningRate)
	{
		super(learningRate);
	}

	public OutputBackpropNeuron(ActivationFunction activationFunction, double learningRate)
	{
		super(activationFunction, learningRate);
	}



    /**
     * This method sets the current training set on the neuron.<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @param trainingToSet sets the current training set.
     */
    public void setDesired(double trainingToSet)
    {
        this.desired = trainingToSet;
    }



    /**
     * Calculates the Delta Train based on all the destination synapses<BR>
     * <!-- Author: Jeffrey Phillips Freeman -->
     * @since 0.1
     * @see com.syncleus.dann.Neuron#backPropagate
     */
    protected void calculateDeltaTrain()
    {
        this.deltaTrain = 0;
        for (Synapse currentSynapse : super.destinations)
			this.deltaTrain += (currentSynapse.getWeight() * this.deltaTrainDestinations.get(currentSynapse).doubleValue());

        this.deltaTrain += (this.desired - this.getOutput());

        this.deltaTrain *= super.activateDerivitive();
    }
    
    public void connectTo(BackpropNeuron outUnit) throws InvalidConnectionTypeDannException
    {
        throw new InvalidConnectionTypeDannException("Can not connect from a OutputNeuron");
    }

	public double getOutput()
	{
		return super.getOutput();
	}
}
