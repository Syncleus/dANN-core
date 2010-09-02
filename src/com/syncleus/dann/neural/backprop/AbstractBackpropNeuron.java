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

public abstract class AbstractBackpropNeuron extends AbstractActivationNeuron implements BackpropNeuron
{
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
	protected final Hashtable<Synapse, Double> deltaTrainDestinations = new Hashtable<Synapse, Double>();

	/**
	 * Creates a new default instance of SimpleBackpropNeuron
	 *
	 * @since 1.0
	 */
	protected AbstractBackpropNeuron(final Brain brain)
	{
		super(brain);
	}

	/**
	 * Creates a new instance of SimpleBackpropNeuron with the specified activation
	 * function.
	 *
	 * @param activationFunction The Neuron's activation function.
	 * @since 1.0
	 */
	protected AbstractBackpropNeuron(final Brain brain, final ActivationFunction activationFunction)
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
	protected AbstractBackpropNeuron(final Brain brain, final double learningRate)
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
	protected AbstractBackpropNeuron(final Brain brain, final ActivationFunction activationFunction, final double learningRate)
	{
		super(brain, activationFunction);
		this.learningRate = learningRate;
	}

	/**
	 * Backpropogates the training data to all the incomming synapses.
	 *
	 * @since 1.0
	 */
	@Override
	public void backPropagate()
	{
		this.calculateDeltaTrain();
		//TODO fix this bad typing
		/*
		//step thru source synapses and make them learn their new weight.
		for(final Synapse currentSynapse : this.getBrain().getInEdges(this))
		{
			final Neuron sourceNeuron = currentSynapse.getSourceNode();
			if( sourceNeuron instanceof BackpropNeuron )
			{
				final BackpropNeuron sourceBackpropNeuron = (BackpropNeuron) sourceNeuron;
				// TODO instead of only working on SimpleBackpropNeuron perhaps make deltaTrain part of a Backprop synapse
				if( sourceBackpropNeuron instanceof SimpleBackpropNeuron )
					((SimpleBackpropNeuron) sourceBackpropNeuron).deltaTrainDestinations.put(currentSynapse, this.deltaTrain);
				currentSynapse.setWeight(currentSynapse.getWeight() + (this.deltaTrain * this.learningRate * currentSynapse.getInput()));
			}
		}
		*/
		//step thru source synapses and make them learn their new weight.
		for(final Object currentSynapse : this.getBrain().getInEdges(this))
		{
			final Neuron sourceNeuron = (Neuron) ((Synapse)currentSynapse).getSourceNode();
			if( sourceNeuron instanceof BackpropNeuron )
			{
				final BackpropNeuron sourceBackpropNeuron = (BackpropNeuron) sourceNeuron;
				// TODO instead of only working on SimpleBackpropNeuron perhaps make deltaTrain part of a Backprop synapse
				if( sourceBackpropNeuron instanceof SimpleBackpropNeuron )
					((SimpleBackpropNeuron) sourceBackpropNeuron).deltaTrainDestinations.put(((Synapse)currentSynapse), this.deltaTrain);
				((Synapse)currentSynapse).setWeight(((Synapse)currentSynapse).getWeight() + (this.deltaTrain * this.learningRate * ((Synapse)currentSynapse).getInput()));
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
		// TODO fix bad typing
		this.deltaTrain = 0;
//		for(final Synapse currentSynapse : this.getBrain().getTraversableEdges(this))
//			this.deltaTrain += (currentSynapse.getWeight() * this.deltaTrainDestinations.get(currentSynapse));
		for(final Object currentSynapse : this.getBrain().getTraversableEdges(this))
			this.deltaTrain += (((Synapse)currentSynapse).getWeight() * this.deltaTrainDestinations.get(currentSynapse));
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
}
