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
package com.syncleus.dann.neural;

import com.syncleus.dann.neural.activation.ActivationFunction;
import com.syncleus.dann.neural.activation.HyperbolicTangentActivationFunction;

/**
 * An abstract implementation of the Neuron interface. Included activation
 * function handling.
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 */
public abstract class AbstractActivationNeuron extends AbstractNeuron
{
	/**
	 * Represents the current excitation of the neuron from input signals.
	 *
	 * @since 1.0
	 */
	private double activity;
	/**
	 * The current activation function used by this neuron. This is used to
	 * calculate the output from the activity.
	 *
	 * @since 1.0
	 */
	private final ActivationFunction activationFunction;
	/**
	 * Represents the current output of the neuron.
	 *
	 * @since 1.0
	 */
	private double output;
	private static final ActivationFunction DEFAULT_ACTIVATION_FUNCTION = new HyperbolicTangentActivationFunction();

	/**
	 * Creates a new instance of NeuronImpl with a RANDOM bias weight and
	 * HyperbolicTangentActivationFunction as the activation function.
	 *
	 * @since 1.0
	 */
	protected AbstractActivationNeuron(final Brain brain)
	{
		super(brain);
		this.activationFunction = DEFAULT_ACTIVATION_FUNCTION;
	}

	/**
	 * Creates a new instance of NEuronImpl with a RANDOM bias weight and the
	 * specified activation function.
	 *
	 * @param activationFunction The activation function used to calculate the
	 * output from the neuron's activity.
	 * @since 1.0
	 */
	protected AbstractActivationNeuron(final Brain brain, final ActivationFunction activationFunction)
	{
		super(brain);
		if( activationFunction == null )
			throw new IllegalArgumentException("activationFunction can not be null");
		this.activationFunction = activationFunction;
	}
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Propogation">

	/**
	 * obtains the output as a function of the current activity. This is a bound
	 * function (usually between -1 and 1) based on the current activity of the
	 * neuron.
	 *
	 * @return a bound value (between -1 and 1 if this function is not
	 *         overwritten). It is a function of the neuron's current activity.
	 * @see com.syncleus.dann.neural.backprop.BackpropNeuron#tick
	 * @since 1.0
	 */
	protected final double activate()
	{
		return this.activationFunction.activate(this.activity);
	}

	/**
	 * This must be the derivity of the ActivityFunction. As such it's output is
	 * also based on the current activity of the neuron. If the activationFunction
	 * is overwritten then this method must also be overwritten with the proper
	 * derivative.
	 *
	 * @return the derivative output of the activationFunction
	 * @see com.syncleus.dann.neural.AbstractActivationNeuron#activationFunction
	 * @since 1.0
	 */
	protected final double activateDerivitive()
	{
		return this.activationFunction.activateDerivative(this.activity);
	}

	/**
	 * Gets the current output.
	 *
	 * @return The current output.
	 * @since 1.0
	 */
	@Override
	protected double getOutput()
	{
		return this.output;
	}

	/**
	 * Propagates the current output to all outgoing synapses.
	 *
	 * @since 1.0
	 */
	@Override
	public void tick()
	{
		//TODO fix this!
		//calculate the current input activity
		this.activity = 0;
//		for(final Synapse currentSynapse : this.getBrain().getInEdges(this))
//			this.activity += currentSynapse.getInput() * currentSynapse.getWeight();
		for(final Object currentSynapse : this.getBrain().getInEdges(this))
			this.activity += ((Synapse)currentSynapse).getInput() * ((Synapse)currentSynapse).getWeight();
		//calculate the activity function and set the result as the output
		this.output = this.activate();
//		for(final Synapse current : this.getBrain().getTraversableEdges(this))
//			current.setInput(this.output);
		for(final Object current : this.getBrain().getTraversableEdges(this))
			((Synapse)current).setInput(this.output);
	}

	protected void setOutput(final double output)
	{
		this.output = output;
	}
}
