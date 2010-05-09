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

import java.util.Hashtable;
import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.activation.ActivationFunction;

/**
 * The SimpleBackpropNeuron is the most fundemental component of a backprop
 * network; it is also the proccessor of the system. One neuron will usually
 * connect to many other Neurons through synapses and receive input from many
 * other Neurons in the same way.
 *
 * @author Jeffrey Phillips Freeman
 * @see com.syncleus.dann.neural.SimpleSynapse
 * @since 1.0
 */
public class SimpleBackpropNeuron extends AbstractActivationNeuron implements BackpropNeuron
{
	// <editor-fold defaultstate="collapsed" desc="Attributes">
	private static final long serialVersionUID = 85919762906996765L;
	/**
	 * This represents the net effect of all the training data from all the
	 * inputs. It is essentially the reverse of the activity value.
	 *
	 * @see com.syncleus.dann.neural.AbstractActivationNeuron#activity
	 * @since 1.0
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
	 * Creates a new default instance of SimpleBackpropNeuron
	 *
	 * @since 1.0
	 */
	public SimpleBackpropNeuron(final Brain brain)
	{
		super(brain);
	}

	/**
	 * Creates a new instance of SimpleBackpropNeuron with the specified
	 * activation function.
	 *
	 * @param activationFunction The Neuron's activation function.
	 * @since 1.0
	 */
	public SimpleBackpropNeuron(final Brain brain, final ActivationFunction activationFunction)
	{
		super(brain, activationFunction);
	}

	/**
	 * Creates a new instance of a SimpleBackpropNeuron using the default
	 * activation function with the specified learning rate.
	 *
	 * @param learningRate learning rate of this neuron.
	 * @since 1.0
	 */
	public SimpleBackpropNeuron(final Brain brain, final double learningRate)
	{
		super(brain);
		this.learningRate = learningRate;
	}

	/**
	 * Creates a new instance of a SimpleBackpropNeuron with the specified
	 * activtion function and learning rate.
	 *
	 * @param activationFunction Activation function for this neuron.
	 * @param learningRate Learning rate for this neuron.
	 */
	public SimpleBackpropNeuron(final Brain brain, final ActivationFunction activationFunction, final double learningRate)
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
		for(final Synapse currentSynapse : this.getBrain().getInEdges(this))
		{
			final Neuron sourceNeuron = currentSynapse.getSourceNode();
			if (sourceNeuron instanceof BackpropNeuron)
			{
				final BackpropNeuron sourceBackpropNeuron = (BackpropNeuron) sourceNeuron;
				// TODO instead of only working on SimpleBackpropNeuron perhaps make deltaTrain part of a Backprop synapse
				if (sourceBackpropNeuron instanceof SimpleBackpropNeuron)
					((SimpleBackpropNeuron) sourceBackpropNeuron).deltaTrainDestinations.put(currentSynapse, Double.valueOf(this.deltaTrain));
				currentSynapse.setWeight(currentSynapse.getWeight() + (this.deltaTrain * this.learningRate * currentSynapse.getInput()));
			}
		}
	}

	/**
	 * Calculates the Delta Train based on all the destination synapses
	 *
	 * @see com.syncleus.dann.neural.backprop.SimpleBackpropNeuron#backPropagate
	 * @since 1.0
	 */
	protected void calculateDeltaTrain()
	{
		this.deltaTrain = 0;
		for(final Synapse currentSynapse : this.getBrain().getTraversableEdges(this))
			this.deltaTrain += (currentSynapse.getWeight() * this.deltaTrainDestinations.get(currentSynapse).doubleValue());
		this.deltaTrain *= this.activateDerivitive();
	}

	/**
	 * Gets the current delta train of the neuron.
	 *
	 * @return The delta train of the neuron.
	 * @since 1.0
	 */
	// TODO put this in the interface and expose as public
	protected double getDeltaTrain()
	{
		return this.deltaTrain;
	}
	// </editor-fold>
}
