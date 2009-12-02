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
package com.syncleus.dann.neural;

import java.util.*;
import com.syncleus.dann.neural.activation.ActivationFunction;
import com.syncleus.dann.neural.activation.HyperbolicTangentActivationFunction;
import org.apache.log4j.Logger;


/**
 * An abstract implementation of the Neuron interface. Included activation
 * function handling.
 *
 *
 * @author Jeffrey Phillips Freeman
 * @param <SN> Source Neurons allowed to connect to this Neuron.
 * @param <DN> Destination Neurons this Neuron is allowed to connect to.
 * @since 1.0
 *
 */
public abstract class AbstractNeuron implements Neuron
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">

    /**
     * Represents the current excitation of the neuron from input
     * signals
     *
     * @since 1.0
     */
    protected double activity;

    /**
     * Represents the current output of the neuron
     *
     * @since 1.0
     */
    protected double output;

    /**
     * The current weight of the bias input. The bias is an input that is always
     * set to an on position. The bias weight usually adapts in the same manner
     * as the rest of the synapse's weights.
     *
     * @since 1.0
     */
    protected double biasWeight;

	/**
	 * The current activation function used by this neuron. This is used to
	 * calculate the output from the activity.
	 *
	 * @since 1.0
	 */
    protected ActivationFunction activationFunction;

	/**
	 * Random number generator used toproduce any needed random values
	 *
	 * @since 1.0
	 */
	protected static final Random RANDOM = new Random();

	private final Brain brain;
	
	private final static HyperbolicTangentActivationFunction DEFAULT_ACTIVATION_FUNCTION = new HyperbolicTangentActivationFunction();
	private final static Logger LOGGER = Logger.getLogger(AbstractNeuron.class);

	protected Brain getBrain()
	{
		return this.brain;
	}

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">

    /**
     * Creates a new instance of NeuronImpl with a random bias weight and
	 * HyperbolicTangentActivationFunction as the activation function.
	 *
     *
     * @since 1.0
     */
    public AbstractNeuron(Brain brain)
    {
		if( brain == null)
			throw new IllegalArgumentException("brain can not be null");

		this.brain = brain;
        this.biasWeight = ((RANDOM.nextDouble() * 2.0) - 1.0) / 1000.0;
        this.activationFunction = DEFAULT_ACTIVATION_FUNCTION;
    }



	/**
	 * Creates a new instance of NEuronImpl with a random bias weight and the
	 * specified activation function.
	 *
	 *
	 * @param activationFunction The activation function used to calculate the
	 * output fromt he neuron's activity.
	 * @since 1.0
	 */
    public AbstractNeuron(Brain brain, ActivationFunction activationFunction)
    {
		if( brain == null)
			throw new IllegalArgumentException("brain can not be null");
        if (activationFunction == null)
            throw new IllegalArgumentException("activationFunction can not be null");

		this.brain = brain;
        this.biasWeight = ((RANDOM.nextDouble() * 2.0) - 1.0) / 1000.0;
        this.activationFunction = activationFunction;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Propogation">

    /**
     * sets the current output on all outgoing synapses.
	 *
     *
     * @since 1.0
     * @see com.syncleus.dann.neural.backprop.BackpropNeuron#propagate
     * @param newOutput The output value.
     */
    protected void setOutput(final double newOutput)
    {
        this.output = newOutput;

        for (Synapse current : this.getBrain().getOutEdges(this))
            current.setInput(newOutput);
    }



    /**
     * Gets the current output.
	 *
     *
     * @since 1.0
     * @return The current output.
     */
    protected double getOutput()
    {
        return this.output;
    }



    /**
     * obtains the output as a function of the current activity. This is a bound
     * function (usually between -1 and 1) based on the current activity of the
     * neuron.
	 *
     *
     * @since 1.0
     * @return a bound value (between -1 and 1 if this function is not
     * 	overwritten). It is a function of the neuron's current activity.
     * @see com.syncleus.dann.neural.backprop.BackpropNeuron#propagate
     */
    protected final double activate()
    {
        return this.activationFunction.activate(this.activity);
    }



    /**
     * This must be the derivity of the ActivityFunction. As such it's output is
     * also based on the current activity of the neuron. If the
     * activationFunction is overwritten then this method must also be
     * overwritten with the proper derivative.
	 *
     *
     * @since 1.0
     * @return the derivative output of the activationFunction
     * @see com.syncleus.dann.neural.NeuronImpl#activationFunction
     */
    protected final double activateDerivitive()
    {
        return this.activationFunction.activateDerivative(this.activity);
    }

	
    /**
     * Propogates the current output to all outgoing synapses.
	 *
     *
     * @since 1.0
     */
    public void propagate()
    {
        //calculate the current input activity
        this.activity = 0;
        for (Synapse currentSynapse : this.brain.getInEdges(this))
            this.activity += currentSynapse.getInput() * currentSynapse.getWeight();
        //Add the bias to the activity
        this.activity += this.biasWeight;

        //calculate the activity function and set the result as the output
        this.setOutput(this.activate());
    }

    // </editor-fold>
}
