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
import com.syncleus.dann.neural.activation.ActivationFunction;
import java.util.Hashtable;


/**
 * The BackpropNeuron is the most fundemental component of a backprop network;
 * it is also the proccessor of the system. One neuron will usually connect to
 * many other Neurons through synapses and receive input from many other
 * Neurons in the same way.
 *
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 *
 * @see com.syncleus.dann.neural.SimpleSynapse
 */
public class BackpropNeuron extends AbstractNeuron//<AbstractNeuron, BackpropNeuron>
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">

    /**
     * This represents the net effect of all the training data from all the
     * inputs. It is essentially the reverse of the activity value.
     *
     * @since 1.0
     * @see com.syncleus.dann.neural.AbstractNeuron#activity
     */
    protected double deltaTrain = 0;

	private double learningRate = 0.001;

	/**
	 * A hashtable which contains the current delta train for each of the
	 * destination synapses.
	 *
	 * @since 1.0
	 */
	protected Hashtable<Synapse, Double> deltaTrainDestinations = new Hashtable<Synapse, Double>();

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">

    /**
     * Creates a new default instance of BackpropNeuron
	 *
     * @since 1.0
     */
    public BackpropNeuron(Brain brain)
    {
		super(brain);
    }



	/**
	 * Creates a new instance of BackpropNeuron with the specified activation
	 * function.
	 *
	 * @param activationFunction The Neuron's activation function.
	 * @since 1.0
	 */
    public BackpropNeuron(Brain brain, ActivationFunction activationFunction)
    {
        super(brain, activationFunction);
    }


	/**
	 * Creates a new instance of a BackpropNeuron using the default activation
	 * function with the specified learning rate.
	 *
	 * @param learningRate learning rate of this neuron.
	 * @since 1.0
	 */
	public BackpropNeuron(Brain brain, double learningRate)
	{
		super(brain);
		this.learningRate = learningRate;
	}

	/**
	 * Creates a new instance of a BackpropNeuron with the specified activtion
	 * function and learning rate.
	 *
	 * @param activationFunction Activation function for this neuron.
	 * @param learningRate Learning rate for this neuron.
	 */
	public BackpropNeuron(Brain brain, ActivationFunction activationFunction, double learningRate)
	{
		super(brain, activationFunction);
		this.learningRate = learningRate;
	}

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">

    /**
     * Backpropogates the training data to all the incomming synapses.
	 *
     * @since 1.0
     */
    public void backPropagate()
    {
        this.calculateDeltaTrain();

        //step thru source synapses and make them learn their new weight.
        for (Synapse currentSynapse : this.getBrain().getInEdges(this))
		{
			Neuron sourceNeuron = currentSynapse.getSourceNode();
			if(sourceNeuron instanceof BackpropNeuron)
			{
				BackpropNeuron sourceBackpropNeuron = (BackpropNeuron) sourceNeuron;

				sourceBackpropNeuron.deltaTrainDestinations.put(currentSynapse, Double.valueOf(this.deltaTrain));

				currentSynapse.setWeight(currentSynapse.getWeight() + (this.deltaTrain * this.learningRate * currentSynapse.getInput()));
			}
		}

        //learn the biases new weight
        this.biasWeight += this.learningRate * this.deltaTrain;
    }



    /**
     * Calculates the Delta Train based on all the destination synapses
	 *
     * @since 1.0
     * @see com.syncleus.dann.neural.backprop.BackpropNeuron#backPropagate
     */
    protected void calculateDeltaTrain()
    {
        this.deltaTrain = 0;
        for (Synapse currentSynapse : this.getBrain().getOutEdges(this))
            this.deltaTrain += (currentSynapse.getWeight() * this.deltaTrainDestinations.get(currentSynapse).doubleValue());
        this.deltaTrain *= this.activateDerivitive();
    }



	/**
	 * Gets the current delta train of the neuron.
	 *
	 * @return The delta train of the neuron.
	 * @since 1.0
	 */
    public double getDeltaTrain()
    {
        return deltaTrain;
    }
    // </editor-fold>
}
